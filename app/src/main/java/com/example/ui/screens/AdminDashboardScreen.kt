package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.NoTransfer
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.DeliveryApplication
import com.example.ui.components.StatCard
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

import com.example.ui.theme.DashboardViewType
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.ViewModule

@Composable
fun AdminDashboardScreen(
    viewModel: DeliveryViewModel,
    onViewDetails: (DeliveryApplication) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalCount by viewModel.totalCount.collectAsStateWithLifecycle()
    val pendingCount by viewModel.pendingCount.collectAsStateWithLifecycle()
    val approvedCount by viewModel.approvedCount.collectAsStateWithLifecycle()
    val rejectedCount by viewModel.rejectedCount.collectAsStateWithLifecycle()

    val currentFilter by viewModel.adminFilter.collectAsStateWithLifecycle()
    val searchQuery by viewModel.adminSearchQuery.collectAsStateWithLifecycle()
    val applications by viewModel.allApplications.collectAsStateWithLifecycle()
    val dashboardViewType by viewModel.dashboardViewType.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(EkartBlueDark)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF22C55E))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "EKART RECRUITMENT ADMIN",
                        letterSpacing = 1.sp,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EkartYellow
                    )
                }
                Text(
                    text = "Recruitment Dashboard",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { viewModel.toggleDesignSheet(true) },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                        .testTag("admin_ui_style_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = "Change UI Style",
                        tint = EkartYellow
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        viewModel.logoutAdmin()
                        onLogout()
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color.White
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Application Overview",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334155)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        title = "Total Received",
                        count = totalCount,
                        icon = Icons.Default.Assignment,
                        accentColor = EkartBluePrimary,
                        isSelected = currentFilter == "ALL",
                        onClick = { viewModel.setAdminFilter("ALL") },
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Pending Review",
                        count = pendingCount,
                        icon = Icons.Default.HourglassTop,
                        accentColor = StatusPending,
                        isSelected = currentFilter == "PENDING",
                        onClick = { viewModel.setAdminFilter("PENDING") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        title = "Approved",
                        count = approvedCount,
                        icon = Icons.Default.CheckCircle,
                        accentColor = StatusApproved,
                        isSelected = currentFilter == "APPROVED",
                        onClick = { viewModel.setAdminFilter("APPROVED") },
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Rejected",
                        count = rejectedCount,
                        icon = Icons.Default.Cancel,
                        accentColor = StatusRejected,
                        isSelected = currentFilter == "REJECTED",
                        onClick = { viewModel.setAdminFilter("REJECTED") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setAdminSearchQuery(it) },
                    placeholder = { Text("Filter by Name, ID, or Phone...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = EkartBluePrimary)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_search_bar"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = EkartBluePrimary,
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filters = listOf(
                        "ALL" to "All (${totalCount})",
                        "PENDING" to "Pending (${pendingCount})",
                        "APPROVED" to "Approved (${approvedCount})",
                        "REJECTED" to "Rejected (${rejectedCount})"
                    )
                    items(filters) { (key, label) ->
                        FilterChip(
                            selected = currentFilter == key,
                            onClick = { viewModel.setAdminFilter(key) },
                            label = { Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EkartBluePrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Applications (${applications.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    // View Type Toggle (Cards vs Table)
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE2E8F0))
                            .padding(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (dashboardViewType == DashboardViewType.CARDS) Color.White else Color.Transparent)
                                .clickable { viewModel.setDashboardViewType(DashboardViewType.CARDS) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .testTag("view_toggle_cards"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ViewModule,
                                    contentDescription = "Cards",
                                    tint = if (dashboardViewType == DashboardViewType.CARDS) EkartBluePrimary else Color(0xFF64748B),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Cards",
                                    fontSize = 11.sp,
                                    fontWeight = if (dashboardViewType == DashboardViewType.CARDS) FontWeight.Bold else FontWeight.Normal,
                                    color = if (dashboardViewType == DashboardViewType.CARDS) EkartBluePrimary else Color(0xFF64748B)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (dashboardViewType == DashboardViewType.TABLE) Color.White else Color.Transparent)
                                .clickable { viewModel.setDashboardViewType(DashboardViewType.TABLE) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .testTag("view_toggle_table"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.TableChart,
                                    contentDescription = "Table",
                                    tint = if (dashboardViewType == DashboardViewType.TABLE) EkartBluePrimary else Color(0xFF64748B),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Table",
                                    fontSize = 11.sp,
                                    fontWeight = if (dashboardViewType == DashboardViewType.TABLE) FontWeight.Bold else FontWeight.Normal,
                                    color = if (dashboardViewType == DashboardViewType.TABLE) EkartBluePrimary else Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }
            }

            if (applications.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No Applications Found",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Try changing search terms or filter selection.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            } else if (dashboardViewType == DashboardViewType.CARDS) {
                items(applications, key = { it.applicationId }) { app ->
                    AdminApplicationItemCard(
                        application = app,
                        onViewDetails = {
                            viewModel.selectApplication(app)
                            onViewDetails(app)
                        }
                    )
                }
            } else {
                items(applications, key = { it.applicationId }) { app ->
                    AdminApplicationTableRow(
                        application = app,
                        onViewDetails = {
                            viewModel.selectApplication(app)
                            onViewDetails(app)
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun AdminApplicationItemCard(
    application: DeliveryApplication,
    onViewDetails: () -> Unit
) {
    val context = LocalContext.current
    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.ENGLISH)
    val submissionDateStr = sdf.format(Date(application.submittedAt))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onViewDetails)
            .testTag("app_card_${application.applicationId}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = application.applicationId,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = EkartBlueDark
                )
                StatusBadge(status = application.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = application.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+91 ${application.mobile}",
                            fontSize = 12.sp,
                            color = Color(0xFF475569)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (application.bikeAvailable) Color(0xFFDCFCE7) else Color(0xFFF1F5F9)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (application.bikeAvailable) Icons.Default.DirectionsBike else Icons.Default.NoTransfer,
                            contentDescription = null,
                            tint = if (application.bikeAvailable) Color(0xFF15803D) else Color(0xFF64748B),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (application.bikeAvailable) "Bike" else "No Bike",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (application.bikeAvailable) Color(0xFF15803D) else Color(0xFF64748B)
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFF1F5F9))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = submissionDateStr,
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${application.mobile}")
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Call applicant",
                            tint = EkartBluePrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Button(
                        onClick = onViewDetails,
                        modifier = Modifier.height(32.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EkartBluePrimary),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("View Details", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminApplicationTableRow(
    application: DeliveryApplication,
    onViewDetails: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onViewDetails)
            .testTag("app_table_row_${application.applicationId}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1.3f)) {
                Text(
                    text = application.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF0F172A),
                    maxLines = 1
                )
                Text(
                    text = application.applicationId,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = EkartBluePrimary
                )
                Text(
                    text = "+91 ${application.mobile}",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (application.bikeAvailable) Color(0xFFDCFCE7) else Color(0xFFF1F5F9))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (application.bikeAvailable) Icons.Default.DirectionsBike else Icons.Default.NoTransfer,
                        contentDescription = null,
                        tint = if (application.bikeAvailable) Color(0xFF15803D) else Color(0xFF64748B),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = if (application.bikeAvailable) "Bike" else "Walk",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (application.bikeAvailable) Color(0xFF15803D) else Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            StatusBadge(status = application.status)

            Spacer(modifier = Modifier.width(4.dp))

            IconButton(
                onClick = onViewDetails,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "View Details",
                    tint = EkartBluePrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
