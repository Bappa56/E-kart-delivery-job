package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "delivery_applications")
data class DeliveryApplication(
    @PrimaryKey
    val applicationId: String = "",
    val name: String = "",
    val mobile: String = "",
    val bikeAvailable: Boolean = true,
    val aadharFrontUrl: String = "",
    val aadharBackUrl: String = "",
    val panFrontUrl: String = "",
    val panBackUrl: String = "",
    val selfieUrl: String = "",
    val submittedAt: Long = System.currentTimeMillis(),
    val trainingDate: String = "",
    val status: String = STATUS_PENDING,
    val rejectionReason: String? = null,
    val approvedAt: Long? = null,
    val rejectedAt: Long? = null
) {
    fun toFirestoreMap(): Map<String, Any?> {
        return mapOf(
            "applicationId" to applicationId,
            "name" to name,
            "mobile" to mobile,
            "bikeAvailable" to bikeAvailable,
            "aadharFrontUrl" to aadharFrontUrl,
            "aadharBackUrl" to aadharBackUrl,
            "panFrontUrl" to panFrontUrl,
            "panBackUrl" to panBackUrl,
            "selfieUrl" to selfieUrl,
            "submittedAt" to submittedAt,
            "trainingDate" to trainingDate,
            "status" to status,
            "rejectionReason" to rejectionReason,
            "approvedAt" to approvedAt,
            "rejectedAt" to rejectedAt
        )
    }

    companion object {
        const val STATUS_PENDING = "PENDING"
        const val STATUS_APPROVED = "APPROVED"
        const val STATUS_REJECTED = "REJECTED"

        fun fromFirestoreMap(map: Map<String, Any?>): DeliveryApplication {
            return DeliveryApplication(
                applicationId = map["applicationId"] as? String ?: "",
                name = map["name"] as? String ?: "",
                mobile = map["mobile"] as? String ?: "",
                bikeAvailable = map["bikeAvailable"] as? Boolean ?: true,
                aadharFrontUrl = map["aadharFrontUrl"] as? String ?: "",
                aadharBackUrl = map["aadharBackUrl"] as? String ?: "",
                panFrontUrl = map["panFrontUrl"] as? String ?: "",
                panBackUrl = map["panBackUrl"] as? String ?: "",
                selfieUrl = map["selfieUrl"] as? String ?: "",
                submittedAt = (map["submittedAt"] as? Number)?.toLong() ?: System.currentTimeMillis(),
                trainingDate = map["trainingDate"] as? String ?: "",
                status = map["status"] as? String ?: STATUS_PENDING,
                rejectionReason = map["rejectionReason"] as? String,
                approvedAt = (map["approvedAt"] as? Number)?.toLong(),
                rejectedAt = (map["rejectedAt"] as? Number)?.toLong()
            )
        }
    }
}
