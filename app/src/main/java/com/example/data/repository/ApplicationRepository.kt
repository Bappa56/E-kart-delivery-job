package com.example.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.data.local.DeliveryApplication
import com.example.data.local.DeliveryApplicationDao
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class ApplicationRepository(
    private val dao: DeliveryApplicationDao,
    private val context: Context
) {
    private val firestore by lazy {
        try {
            Firebase.firestore
        } catch (e: Exception) {
            Log.e("EkartRepo", "Firestore init exception", e)
            null
        }
    }

    val allApplications: Flow<List<DeliveryApplication>> = dao.getAllApplications()

    fun getApplicationById(id: String): Flow<DeliveryApplication?> = dao.getApplicationById(id)

    fun searchApplications(query: String): Flow<List<DeliveryApplication>> =
        dao.searchByMobileOrId(query)

    val totalCount: Flow<Int> = dao.getTotalCount()
    val pendingCount: Flow<Int> = dao.getPendingCount()
    val approvedCount: Flow<Int> = dao.getApprovedCount()
    val rejectedCount: Flow<Int> = dao.getRejectedCount()

    init {
        startFirestoreSync()
    }

    private fun startFirestoreSync() {
        val fs = firestore ?: return
        try {
            fs.collection("applications")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w("EkartRepo", "Firestore snapshot listener error: ${error.message}")
                        return@addSnapshotListener
                    }
                    if (snapshot != null && !snapshot.isEmpty) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val list = snapshot.documents.mapNotNull { doc ->
                                doc.data?.let { DeliveryApplication.fromFirestoreMap(it) }
                            }
                            if (list.isNotEmpty()) {
                                dao.insertApplications(list)
                            }
                        }
                    }
                }
        } catch (e: Exception) {
            Log.e("EkartRepo", "Error starting sync listener", e)
        }
    }

    suspend fun submitApplication(
        name: String,
        mobile: String,
        bikeAvailable: Boolean,
        aadharFrontUri: String,
        aadharBackUri: String,
        panFrontUri: String,
        panBackUri: String,
        selfieUri: String
    ): DeliveryApplication = withContext(Dispatchers.IO) {
        val applicationId = generateApplicationId()
        val trainingDate = calculateTomorrowMorningDate()

        // Copy images to secure sandboxed internal app storage
        val secureAadharFront = persistDocument(applicationId, "aadhar_front", aadharFrontUri)
        val secureAadharBack = persistDocument(applicationId, "aadhar_back", aadharBackUri)
        val securePanFront = persistDocument(applicationId, "pan_front", panFrontUri)
        val securePanBack = persistDocument(applicationId, "pan_back", panBackUri)
        val secureSelfie = persistDocument(applicationId, "selfie", selfieUri)

        val application = DeliveryApplication(
            applicationId = applicationId,
            name = name.trim(),
            mobile = mobile.trim(),
            bikeAvailable = bikeAvailable,
            aadharFrontUrl = secureAadharFront,
            aadharBackUrl = secureAadharBack,
            panFrontUrl = securePanFront,
            panBackUrl = securePanBack,
            selfieUrl = secureSelfie,
            submittedAt = System.currentTimeMillis(),
            trainingDate = trainingDate,
            status = DeliveryApplication.STATUS_PENDING
        )

        // Persist locally first for offline instant responsiveness
        dao.insertApplication(application)

        // Persist to Firebase Firestore
        try {
            firestore?.collection("applications")
                ?.document(applicationId)
                ?.set(application.toFirestoreMap())
                ?.await()
            Log.d("EkartRepo", "Successfully synced $applicationId to Firestore")
        } catch (e: Exception) {
            Log.w("EkartRepo", "Firestore sync postponed (offline): ${e.message}")
        }

        application
    }

    suspend fun approveApplication(applicationId: String) = withContext(Dispatchers.IO) {
        val app = dao.getApplicationById(applicationId).first() ?: return@withContext
        val updated = app.copy(
            status = DeliveryApplication.STATUS_APPROVED,
            approvedAt = System.currentTimeMillis(),
            rejectionReason = null,
            trainingDate = calculateTomorrowMorningDate()
        )
        dao.updateApplication(updated)

        try {
            firestore?.collection("applications")
                ?.document(applicationId)
                ?.set(updated.toFirestoreMap())
                ?.await()
        } catch (e: Exception) {
            Log.w("EkartRepo", "Firestore update failed: ${e.message}")
        }
    }

    suspend fun rejectApplication(applicationId: String, reason: String) = withContext(Dispatchers.IO) {
        val app = dao.getApplicationById(applicationId).first() ?: return@withContext
        val updated = app.copy(
            status = DeliveryApplication.STATUS_REJECTED,
            rejectedAt = System.currentTimeMillis(),
            rejectionReason = reason.trim(),
            approvedAt = null
        )
        dao.updateApplication(updated)

        try {
            firestore?.collection("applications")
                ?.document(applicationId)
                ?.set(updated.toFirestoreMap())
                ?.await()
        } catch (e: Exception) {
            Log.w("EkartRepo", "Firestore update failed: ${e.message}")
        }
    }

    private fun persistDocument(appId: String, type: String, sourceUriString: String): String {
        return try {
            val uri = Uri.parse(sourceUriString)
            val docDir = File(context.filesDir, "ekart_documents").apply { mkdirs() }
            val destFile = File(docDir, "${appId}_${type}.jpg")

            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
            destFile.absolutePath
        } catch (e: Exception) {
            sourceUriString
        }
    }

    suspend fun seedSampleDataIfNeeded() = withContext(Dispatchers.IO) {
        val existing = dao.getAllApplications().first()
        if (existing.isEmpty()) {
            val now = System.currentTimeMillis()
            val tomorrowTraining = calculateTomorrowMorningDate()

            val samples = listOf(
                DeliveryApplication(
                    applicationId = "EKART-749102",
                    name = "Rahul Sharma",
                    mobile = "9876543210",
                    bikeAvailable = true,
                    aadharFrontUrl = "demo_aadhar_front",
                    aadharBackUrl = "demo_aadhar_back",
                    panFrontUrl = "demo_pan_front",
                    panBackUrl = "demo_pan_back",
                    selfieUrl = "demo_selfie_1",
                    submittedAt = now - 3600000L * 2,
                    trainingDate = tomorrowTraining,
                    status = DeliveryApplication.STATUS_PENDING
                ),
                DeliveryApplication(
                    applicationId = "EKART-829471",
                    name = "Amit Kumar Patel",
                    mobile = "9812345678",
                    bikeAvailable = true,
                    aadharFrontUrl = "demo_aadhar_front",
                    aadharBackUrl = "demo_aadhar_back",
                    panFrontUrl = "demo_pan_front",
                    panBackUrl = "demo_pan_back",
                    selfieUrl = "demo_selfie_2",
                    submittedAt = now - 86400000L,
                    trainingDate = tomorrowTraining,
                    status = DeliveryApplication.STATUS_APPROVED,
                    approvedAt = now - 3600000L * 4
                ),
                DeliveryApplication(
                    applicationId = "EKART-630182",
                    name = "Vikas Verma",
                    mobile = "9765432109",
                    bikeAvailable = false,
                    aadharFrontUrl = "demo_aadhar_front",
                    aadharBackUrl = "demo_aadhar_back",
                    panFrontUrl = "demo_pan_front",
                    panBackUrl = "demo_pan_back",
                    selfieUrl = "demo_selfie_3",
                    submittedAt = now - 86400000L * 2,
                    trainingDate = tomorrowTraining,
                    status = DeliveryApplication.STATUS_REJECTED,
                    rejectedAt = now - 86400000L,
                    rejectionReason = "Aadhaar Card front image is too blurry. Details unreadable. Please re-upload clear photos."
                )
            )
            dao.insertApplications(samples)

            // Sync initial sample data to Firestore as well
            try {
                samples.forEach { sample ->
                    firestore?.collection("applications")
                        ?.document(sample.applicationId)
                        ?.set(sample.toFirestoreMap())
                }
            } catch (e: Exception) {
                Log.w("EkartRepo", "Failed to seed to Firestore: ${e.message}")
            }
        }
    }

    companion object {
        fun generateApplicationId(): String {
            val randomNum = Random.nextInt(100000, 999999)
            return "EKART-$randomNum"
        }

        fun calculateTomorrowMorningDate(): String {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            val dayFormat = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.ENGLISH)
            return "Tomorrow Morning (${dayFormat.format(calendar.time)} • 09:30 AM)"
        }
    }
}
