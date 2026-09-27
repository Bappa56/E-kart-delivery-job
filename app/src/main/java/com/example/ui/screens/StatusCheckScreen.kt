package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.DeliveryApplication
import com.example.ui.components.StatusBadge
import com.example.ui.theme.EkartBlueDark
import com.example.ui.theme.EkartBluePrimary
import com.example.ui.theme.EkartYellow
import com.example.ui.theme.StatusApproved
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StatusRejected
import com.example.ui.viewmodel.DeliveryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StatusCheckScreen(
    viewModel: DeliveryViewModel,
    onApplyNew: () -> Unit,
    modifier: Modifier = Modifier
) {
    val query by viewModel.statusQuery.collectAsStateWithLifecycle()
    val results by viewModel.statusSearchResults.collectAsStateWithLifecycle()
    val hasSearched by viewModel.hasSearchedStatus.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearchingStatus.collectAsStateWithLifecycle()

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp)
    ) {
        // Screen Title Header
        Column(modifier = Modifier.padding(vertical = 6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(EkartBluePrimary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = EkartBluePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Track Application Status",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = EkartBlueDark
                    )
                    Text(
                        text = "Real-time verification & onboarding tracker",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Input Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Text(
                    text = "Search by Mobile Number or Application ID:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF334155)
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = query,
                    onValueChange = { viewModel.updateStatusQuery(it) },
                    placeholder = { Text("e.g. 9876543210 or EKART-849201") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = EkartBluePrimary)
                    },
                    trailingIcon = {
                        if (query.isNotBlank()) {
                            IconButton(onClick = { viewModel.updateStatusQuery("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color(0xFF94A3B8))
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { viewModel.searchStatus() }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("status_search_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EkartBluePrimary,
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { viewModel.searchStatus() },
                    enabled = query.isNotBlank() && !isSearching,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("status_search_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EkartBluePrimary)
                ) {
                    if (isSearching) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Looking up application...", fontWeight = FontWeight.Bold)
                    } else {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Track Application Status", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Demo Search Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Quick Demo Check:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.width(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                item {
                    DemoSearchChip(
                        label = "Pending ⏳",
                        value = "9876543210",
                        onClick = {
                            viewModel.updateStatusQuery("9876543210")
                            viewModel.searchStatus()
                        }
                    )
                }
                item {
                    DemoSearchChip(
                        label = "Approved 🎉",
                        value = "9812345678",
                        onClick = {
                            viewModel.updateStatusQuery("9812345678")
                            viewModel.searchStatus()
                        }
                    )
                }
                item {
                    DemoSearchChip(
                        label = "Rejected ❌",
                        value = "9765432109",
                        onClick = {
                            viewModel.updateStatusQuery("9765432109")
                            viewModel.searchStatus()
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Results
        if (hasSearched) {
            if (results.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFEE2E2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No Application Found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF334155)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "No active onboarding application found for '$query'. Please ensure the 10-digit mobile number or Application ID is correct.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onApplyNew,
                            colors = ButtonDefaults.buttonColors(containerColor = EkartBluePrimary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Submit New Application", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(results, key = { it.applicationId }) { app ->
                        ComprehensiveStatusCard(
                            application = app,
                            onReApply = onApplyNew
                        )
                    }
                }
            }
        } else {
            // Default Welcome & Guidance Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = EkartBluePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "How Application Tracking Works",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF1E293B)
                        )
                    }

                    Text(
                        text = "1. Enter the mobile number you used during registration or your unique Application ID (e.g. EKART-948201).\n" +
                                "2. Review your live document verification status.\n" +
                                "3. If approved, your reporting training center and schedule will be confirmed for Tomorrow Morning.\n" +
                                "4. In case of document rejection, you can re-upload corrected documents immediately.",
                        fontSize = 12.sp,
                        color = Color(0xFF475569),
                        lineHeight = 18.sp
                    )

                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Haven't applied yet?",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                        OutlinedButton(
                            onClick = onApplyNew,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Apply Now", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ComprehensiveStatusCard(
    application: DeliveryApplication,
    onReApply: () -> Unit
) {
    val context = LocalContext.current
    val sdf = SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.ENGLISH)
    val formattedDate = sdf.format(Date(application.submittedAt))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("status_result_card_${application.applicationId}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header with ID & Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "APPLICATION ID",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = Color(0xFF64748B)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = application.applicationId,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = EkartBlueDark
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Application ID", application.applicationId)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Application ID copied to clipboard", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy ID",
                                tint = EkartBluePrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                StatusBadge(status = application.status)
            }

            HorizontalDivider(color = Color(0xFFF1F5F9))

            // Applicant Summary Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Candidate Name", fontSize = 11.sp, color = Color(0xFF64748B))
                    Text(
                        text = application.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("Registered Mobile", fontSize = 11.sp, color = Color(0xFF64748B))
                    Text(
                        text = "+91 ${application.mobile}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (application.bikeAvailable) Icons.Default.DirectionsBike else Icons.Default.Person,
                        contentDescription = null,
                        tint = if (application.bikeAvailable) Color(0xFF15803D) else Color(0xFF64748B),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (application.bikeAvailable) "Two-Wheeler Available" else "Bicycle / Metro Walker",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (application.bikeAvailable) Color(0xFF15803D) else Color(0xFF475569)
                    )
                }

                Text(
                    text = "Applied: $formattedDate",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )
            }

            HorizontalDivider(color = Color(0xFFF1F5F9))

            // 4-STAGE TIMELINE TRACKER
            Text(
                text = "ONBOARDING TIMELINE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                TimelineItem(
                    stepNumber = "1",
                    title = "Application & Documents Submitted",
                    subtitle = "Aadhaar, PAN & Selfie received securely",
                    isComplete = true,
                    isActive = false,
                    isFailed = false
                )
                TimelineItem(
                    stepNumber = "2",
                    title = "Identity & Background Verification",
                    subtitle = when (application.status) {
                        DeliveryApplication.STATUS_APPROVED -> "Identity documents verified successfully"
                        DeliveryApplication.STATUS_REJECTED -> "Verification failed — issues detected"
                        else -> "In progress with compliance officers"
                    },
                    isComplete = application.status == DeliveryApplication.STATUS_APPROVED,
                    isActive = application.status == DeliveryApplication.STATUS_PENDING,
                    isFailed = application.status == DeliveryApplication.STATUS_REJECTED
                )
                TimelineItem(
                    stepNumber = "3",
                    title = "Admin Review & Approval",
                    subtitle = when (application.status) {
                        DeliveryApplication.STATUS_APPROVED -> "Approved for Ekart Delivery Fleet"
                        DeliveryApplication.STATUS_REJECTED -> "Application Rejected"
                        else -> "Pending final manager approval"
                    },
                    isComplete = application.status == DeliveryApplication.STATUS_APPROVED,
                    isActive = application.status == DeliveryApplication.STATUS_PENDING,
                    isFailed = application.status == DeliveryApplication.STATUS_REJECTED
                )
                TimelineItem(
                    stepNumber = "4",
                    title = "Training & Kit Issuance",
                    subtitle = if (application.status == DeliveryApplication.STATUS_APPROVED) {
                        "Scheduled: ${application.trainingDate}"
                    } else if (application.status == DeliveryApplication.STATUS_PENDING) {
                        "Scheduled for Tomorrow Morning upon approval"
                    } else {
                        "Training cancelled due to rejection"
                    },
                    isComplete = false,
                    isActive = application.status == DeliveryApplication.STATUS_APPROVED,
                    isFailed = application.status == DeliveryApplication.STATUS_REJECTED
                )
            }

            // Status-Specific Action Callouts
            when (application.status) {
                DeliveryApplication.STATUS_APPROVED -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF10B981))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF15803D),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "🎉 Approved! Welcome to Ekart Logistics",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF166534)
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = Color(0xFF047857),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Training Date: ${application.trainingDate}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF065F46)
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color(0xFF047857),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Reporting Center: Ekart Central Logistics Distribution Hub",
                                    fontSize = 12.sp,
                                    color = Color(0xFF047857)
                                )
                            }

                            Text(
                                text = "Mandatory checklist for training day:\n" +
                                        "• Carry original Aadhaar & PAN Card for verification\n" +
                                        "• Smartphone with active internet connection\n" +
                                        "• Bank account passbook or cancelled cheque for payouts",
                                fontSize = 11.sp,
                                color = Color(0xFF1E3A8A),
                                lineHeight = 16.sp
                            )

                            // Action buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        val sendIntent: Intent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(
                                                Intent.EXTRA_TEXT,
                                                "Ekart Logistics Delivery Partner Onboarding Pass\n" +
                                                        "Applicant: ${application.name}\n" +
                                                        "Application ID: ${application.applicationId}\n" +
                                                        "Status: APPROVED\n" +
                                                        "Training Date: ${application.trainingDate}"
                                            )
                                            type = "text/plain"
                                        }
                                        context.startActivity(Intent.createChooser(sendIntent, "Share Ekart Pass"))
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Share Slip", fontSize = 12.sp)
                                }

                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL).apply {
                                            data = Uri.parse("tel:18002089898")
                                        }
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Call Hub", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                DeliveryApplication.STATUS_REJECTED -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFEF4444))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Cancel,
                                    contentDescription = null,
                                    tint = Color(0xFFDC2626),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Application Rejected",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF991B1B)
                                )
                            }

                            Text(
                                text = "Reason for Rejection:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFF7F1D1D)
                            )

                            Text(
                                text = application.rejectionReason
                                    ?: "Identity document images were blurred or did not match name. Please re-upload clear photos.",
                                fontSize = 12.sp,
                                color = Color(0xFFB91C1C),
                                lineHeight = 16.sp
                            )

                            Text(
                                text = "Note: Training confirmation is not issued for rejected applications.",
                                fontSize = 11.sp,
                                color = Color(0xFF991B1B)
                            )

                            Button(
                                onClick = onReApply,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Re-Apply with Corrected Documents", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                DeliveryApplication.STATUS_PENDING -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF59E0B))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.HourglassTop,
                                    contentDescription = null,
                                    tint = StatusPending,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Application Under Review",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF92400E)
                                )
                            }

                            Text(
                                text = "Your application has been submitted successfully. Please wait for admin approval.",
                                fontSize = 12.sp,
                                color = Color(0xFF78350F),
                                lineHeight = 16.sp
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = Color(0xFFB45309),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Estimated Training: Tomorrow Morning (pending approval)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF92400E)
                                )
                            }

                            Text(
                                text = "Review turn-around time is typically within 2 to 4 hours during business days.",
                                fontSize = 11.sp,
                                color = Color(0xFF78350F)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TimelineItem(
    stepNumber: String,
    title: String,
    subtitle: String,
    isComplete: Boolean,
    isActive: Boolean,
    isFailed: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isFailed -> Color(0xFFDC2626)
                        isComplete -> Color(0xFF15803D)
                        isActive -> EkartBluePrimary
                        else -> Color(0xFFE2E8F0)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            when {
                isFailed -> Icon(Icons.Default.Cancel, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                isComplete -> Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                else -> Text(
                    text = stepNumber,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isActive) Color.White else Color(0xFF64748B)
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = if (isActive || isComplete) FontWeight.Bold else FontWeight.Medium,
                fontSize = 12.sp,
                color = when {
                    isFailed -> Color(0xFFDC2626)
                    isComplete -> Color(0xFF15803D)
                    isActive -> EkartBlueDark
                    else -> Color(0xFF64748B)
                }
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Color(0xFF64748B)
            )
        }
    }
}

@Composable
private fun DemoSearchChip(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFFE2E8F0))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )
    }
}
