package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.local.DeliveryApplication
import com.example.ui.components.StatusBadge
import com.example.ui.theme.EkartBlueDark
import com.example.ui.theme.EkartBluePrimary
import com.example.ui.theme.StatusApproved
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StatusRejected
import com.example.ui.viewmodel.DeliveryViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ApplicationDetailScreen(
    application: DeliveryApplication,
    viewModel: DeliveryViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var showApproveConfirmDialog by remember { mutableStateOf(false) }
    var showRejectReasonDialog by remember { mutableStateOf(false) }
    var rejectionReasonInput by remember { mutableStateOf("") }
    var rejectionError by remember { mutableStateOf<String?>(null) }

    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.ENGLISH)
    val submittedDateStr = sdf.format(Date(application.submittedAt))

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(EkartBlueDark)
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(
                    text = "Application Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = application.applicationId,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF93C5FD)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Current Review Status", fontSize = 11.sp, color = Color(0xFF64748B))
                            Text(
                                text = application.status,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (application.status) {
                                    DeliveryApplication.STATUS_APPROVED -> StatusApproved
                                    DeliveryApplication.STATUS_REJECTED -> StatusRejected
                                    else -> StatusPending
                                }
                            )
                        }
                        StatusBadge(status = application.status)
                    }

                    if (application.status == DeliveryApplication.STATUS_REJECTED && application.rejectionReason != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFEF2F2))
                                .padding(10.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Rejection Reason:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF991B1B)
                                )
                                Text(
                                    text = application.rejectionReason ?: "",
                                    fontSize = 12.sp,
                                    color = Color(0xFF7F1D1D)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { showApproveConfirmDialog = true },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("btn_approve_application"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = StatusApproved,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Approve", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Button(
                            onClick = {
                                rejectionError = null
                                showRejectReasonDialog = true
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("btn_reject_application"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = StatusRejected,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Reject", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Applicant Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EkartBlueDark
                    )

                    DetailInfoRow(label = "Full Name", value = application.name)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Mobile Number", fontSize = 12.sp, color = Color(0xFF64748B))
                            Text("+91 ${application.mobile}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${application.mobile}")
                                }
                                context.startActivity(intent)
                            },
                            modifier = Modifier.height(34.dp),
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEFF6FF), contentColor = EkartBluePrimary)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Call", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    DetailInfoRow(
                        label = "Two-Wheeler Available",
                        value = if (application.bikeAvailable) "Yes (Motorcycle / Scooter)" else "No (Cycle / On Foot)"
                    )

                    DetailInfoRow(label = "Submitted At", value = submittedDateStr)

                    DetailInfoRow(label = "Scheduled Training", value = application.trainingDate)

                    if (application.approvedAt != null) {
                        val approvedDateStr = sdf.format(Date(application.approvedAt))
                        DetailInfoRow(label = "Approved At", value = approvedDateStr)
                    }

                    if (application.rejectedAt != null) {
                        val rejectedDateStr = sdf.format(Date(application.rejectedAt))
                        DetailInfoRow(label = "Rejected At", value = rejectedDateStr)
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Uploaded Verification Documents",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EkartBlueDark
                        )
                        Text(
                            text = "Tap to Zoom",
                            fontSize = 12.sp,
                            color = EkartBluePrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    DocumentInspectionTile(
                        title = "1. Aadhar Card (Front)",
                        uriOrPath = application.aadharFrontUrl,
                        onZoom = { viewModel.openZoom("Aadhar Card Front", application.aadharFrontUrl) }
                    )

                    DocumentInspectionTile(
                        title = "2. Aadhar Card (Back)",
                        uriOrPath = application.aadharBackUrl,
                        onZoom = { viewModel.openZoom("Aadhar Card Back", application.aadharBackUrl) }
                    )

                    DocumentInspectionTile(
                        title = "3. PAN Card (Front)",
                        uriOrPath = application.panFrontUrl,
                        onZoom = { viewModel.openZoom("PAN Card Front", application.panFrontUrl) }
                    )

                    DocumentInspectionTile(
                        title = "4. PAN Card (Back)",
                        uriOrPath = application.panBackUrl,
                        onZoom = { viewModel.openZoom("PAN Card Back", application.panBackUrl) }
                    )

                    DocumentInspectionTile(
                        title = "5. Applicant Selfie Photo",
                        uriOrPath = application.selfieUrl,
                        onZoom = { viewModel.openZoom("Applicant Selfie Photo", application.selfieUrl) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showApproveConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showApproveConfirmDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = StatusApproved,
                    modifier = Modifier.size(40.dp)
                )
            },
            title = {
                Text("Approve Application?")
            },
            text = {
                Column {
                    Text("Are you sure you want to approve ${application.name}'s delivery partner onboarding?")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Training will be officially confirmed for:\n${application.trainingDate}",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF065F46)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.approveApplication(application.applicationId) {
                            showApproveConfirmDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusApproved)
                ) {
                    Text("Confirm Approval")
                }
            },
            dismissButton = {
                TextButton(onClick = { showApproveConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showRejectReasonDialog) {
        AlertDialog(
            onDismissRequest = { showRejectReasonDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = null,
                    tint = StatusRejected,
                    modifier = Modifier.size(40.dp)
                )
            },
            title = {
                Text("Reject Application")
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Please provide the specific reason for rejecting this candidate's application:")

                    val quickReasons = listOf(
                        "Aadhaar front image is blurry or unreadable",
                        "PAN Card number mismatch or illegible",
                        "Selfie photo is not clear / face obstructed",
                        "Missing address proof in Aadhaar back"
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        quickReasons.forEach { reason ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .clickable { rejectionReasonInput = reason }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "• $reason",
                                    fontSize = 11.sp,
                                    color = Color(0xFF334155)
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = rejectionReasonInput,
                        onValueChange = {
                            rejectionReasonInput = it
                            rejectionError = null
                        },
                        label = { Text("Rejection Reason *") },
                        placeholder = { Text("Enter explanation for candidate...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("rejection_reason_input"),
                        minLines = 3,
                        maxLines = 4
                    )

                    if (rejectionError != null) {
                        Text(
                            text = rejectionError ?: "",
                            color = Color(0xFFDC2626),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (rejectionReasonInput.trim().isEmpty()) {
                            rejectionError = "Rejection reason is required."
                            return@Button
                        }
                        viewModel.rejectApplication(application.applicationId, rejectionReasonInput) {
                            showRejectReasonDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusRejected)
                ) {
                    Text("Reject Application")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRejectReasonDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun DetailInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = Color(0xFF64748B))
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
    }
}

@Composable
fun DocumentInspectionTile(
    title: String,
    uriOrPath: String,
    onZoom: () -> Unit
) {
    val context = LocalContext.current
    val isDemo = uriOrPath.startsWith("demo_")
    val file = File(uriOrPath)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onZoom),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFE2E8F0)),
                contentAlignment = Alignment.Center
            ) {
                if (isDemo) {
                    Icon(
                        imageVector = Icons.Default.ZoomIn,
                        contentDescription = null,
                        tint = EkartBluePrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    val model = if (file.exists()) file else uriOrPath
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(model)
                            .crossfade(true)
                            .build(),
                        contentDescription = title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = if (isDemo) "Demo Verified Specimen" else "Uploaded Document File",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }

            IconButton(onClick = onZoom) {
                Icon(
                    imageVector = Icons.Default.ZoomIn,
                    contentDescription = "Zoom",
                    tint = EkartBluePrimary
                )
            }
        }
    }
}
