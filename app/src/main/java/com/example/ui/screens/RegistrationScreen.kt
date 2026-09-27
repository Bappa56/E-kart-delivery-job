package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NoTransfer
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PedalBike
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.components.DocumentUploadCard
import com.example.ui.components.UiDesignSettingsDialog
import com.example.ui.theme.UiLayoutType
import com.example.ui.viewmodel.DeliveryViewModel
import com.example.ui.viewmodel.RegistrationFormState

private data class VehicleOption(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val hasBike: Boolean
)

@Composable
fun RegistrationScreen(
    viewModel: DeliveryViewModel,
    onApplicationSubmitted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.registrationState.collectAsStateWithLifecycle()
    val currentTheme by viewModel.currentUiTheme.collectAsStateWithLifecycle()
    val currentLayout by viewModel.currentLayoutType.collectAsStateWithLifecycle()
    val dashboardView by viewModel.dashboardViewType.collectAsStateWithLifecycle()
    val showDesignDialog by viewModel.showDesignSheet.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    val vehicleOptions = remember {
        listOf(
            VehicleOption("Motorcycle / Bike", "Petrol Two-Wheeler (100cc+)", Icons.Default.DirectionsBike, true),
            VehicleOption("EV Electric Scooter", "Eco Battery Two-Wheeler", Icons.Default.ElectricScooter, true),
            VehicleOption("Bicycle / E-Bike", "Local Hub Courier Delivery", Icons.Default.PedalBike, false),
            VehicleOption("3-Wheeler Mini Cargo", "Commercial Loader / Auto", Icons.Default.LocalShipping, true),
            VehicleOption("On-Foot Walker", "Metro & High-rise Hubs", Icons.Default.Person, false)
        )
    }

    val cityHubs = remember {
        listOf(
            "Bengaluru Central Hub",
            "Delhi NCR Logistics Center",
            "Mumbai Metro Delivery Hub",
            "Hyderabad Tech Express Hub",
            "Kolkata City Central Hub",
            "Chennai Coastal Hub"
        )
    }

    val shiftOptions = remember {
        listOf(
            "Full-Time Partner (9 AM - 6 PM)",
            "Part-Time Evening (5 PM - 11 PM)",
            "Weekend Express Delivery"
        )
    }

    if (showDesignDialog) {
        UiDesignSettingsDialog(
            selectedTheme = currentTheme,
            selectedLayout = currentLayout,
            selectedDashboardView = dashboardView,
            onThemeSelected = { viewModel.setUiDesignTheme(it) },
            onLayoutSelected = { viewModel.setUiLayoutType(it) },
            onDashboardViewSelected = { viewModel.setDashboardViewType(it) },
            onDismiss = { viewModel.toggleDesignSheet(false) }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Hero Banner Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_delivery_hero),
                contentDescription = "Ekart Logistics Delivery Partner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xDD0B1120)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.secondary)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "JOIN EKART",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Delivery Agent Registration Portal",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Delivery Boy Job Registration",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Earn up to ₹30,000/month with daily incentives & training",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 12.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Error banner if any
            if (state.errorMessage != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = state.errorMessage ?: "",
                            color = Color(0xFFB91C1C),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Quick UI Theme Style switcher bar
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ui_design_type_switcher_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = "UI Theme",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Form Layout & Theme",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${currentTheme.displayName} • ${currentLayout.title}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Button(
                        onClick = { viewModel.toggleDesignSheet(true) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("change_ui_design_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Style", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Render Form depending on selected layout type (Default: 4-Step Guided Flow)
            when (currentLayout) {
                UiLayoutType.STEPPER_WIZARD -> {
                    MultiStepWizardForm(
                        state = state,
                        vehicleOptions = vehicleOptions,
                        cityHubs = cityHubs,
                        shiftOptions = shiftOptions,
                        viewModel = viewModel,
                        onApplicationSubmitted = onApplicationSubmitted
                    )
                }

                UiLayoutType.CLASSIC_ALL_IN_ONE -> {
                    ClassicContinuousForm(
                        state = state,
                        vehicleOptions = vehicleOptions,
                        cityHubs = cityHubs,
                        shiftOptions = shiftOptions,
                        viewModel = viewModel,
                        onApplicationSubmitted = onApplicationSubmitted
                    )
                }

                UiLayoutType.EXPANDABLE_SECTIONS -> {
                    AccordionSectionsForm(
                        state = state,
                        vehicleOptions = vehicleOptions,
                        cityHubs = cityHubs,
                        shiftOptions = shiftOptions,
                        viewModel = viewModel,
                        onApplicationSubmitted = onApplicationSubmitted
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------
// 1. MULTI-STEP WIZARD FORM (Step 1 -> 2 -> 3 -> 4)
// ---------------------------------------------------------
@Composable
private fun MultiStepWizardForm(
    state: RegistrationFormState,
    vehicleOptions: List<VehicleOption>,
    cityHubs: List<String>,
    shiftOptions: List<String>,
    viewModel: DeliveryViewModel,
    onApplicationSubmitted: () -> Unit
) {
    val currentStep = state.wizardStep

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Step Indicators Bar
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Step $currentStep of 4",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = when (currentStep) {
                            1 -> "25% Completed"
                            2 -> "50% Completed"
                            3 -> "75% Completed"
                            else -> "100% Ready"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { currentStep.toFloat() / 4f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color(0xFFE2E8F0)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    WizardStepPill(
                        stepNumber = 1,
                        label = "Personal",
                        isActive = currentStep == 1,
                        isDone = currentStep > 1,
                        onClick = { viewModel.setWizardStep(1) }
                    )
                    WizardStepPill(
                        stepNumber = 2,
                        label = "Vehicle",
                        isActive = currentStep == 2,
                        isDone = currentStep > 2,
                        onClick = {
                            if (state.isStep1Valid) viewModel.setWizardStep(2)
                            else viewModel.nextStep()
                        }
                    )
                    WizardStepPill(
                        stepNumber = 3,
                        label = "KYC Docs",
                        isActive = currentStep == 3,
                        isDone = currentStep > 3 || state.uploadedDocumentsCount == 5,
                        onClick = {
                            if (state.isStep1Valid) viewModel.setWizardStep(3)
                            else viewModel.nextStep()
                        }
                    )
                    WizardStepPill(
                        stepNumber = 4,
                        label = "Submit",
                        isActive = currentStep == 4,
                        isDone = state.successApplication != null,
                        onClick = {
                            if (state.isStep1Valid && state.uploadedDocumentsCount == 5) {
                                viewModel.setWizardStep(4)
                            } else {
                                viewModel.nextStep()
                            }
                        }
                    )
                }
            }
        }

        // STEP 1: Personal Details & City/Hub
        if (currentStep == 1) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("1", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Personal & Contact Details",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Input your official identity details as per government ID",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = state.name,
                        onValueChange = { viewModel.updateName(it) },
                        label = { Text("Full Name (as per Aadhaar Card)*") },
                        placeholder = { Text("e.g. Ramesh Kumar Sharma") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_full_name"),
                        shape = RoundedCornerShape(8.dp)
                    )

                    OutlinedTextField(
                        value = state.mobile,
                        onValueChange = { viewModel.updateMobile(it) },
                        label = { Text("Mobile Number (10 Digits)*") },
                        placeholder = { Text("9876543210") },
                        leadingIcon = {
                            Row(
                                modifier = Modifier.padding(start = 12.dp, end = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "+91",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_mobile_number"),
                        shape = RoundedCornerShape(8.dp)
                    )

                    // City Hub selection
                    Text(
                        text = "Preferred Ekart Logistics Hub / Location",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        cityHubs.take(4).forEach { hub ->
                            val isSelected = state.cityHub == hub
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { viewModel.setCityHub(hub) },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                                    else Color(0xFFF8FAFC)
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    if (isSelected) 1.5.dp else 1.dp,
                                    if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFE2E8F0)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF64748B),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = hub,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF1E293B)
                                    )
                                }
                            }
                        }
                    }

                    // Shift selection
                    Text(
                        text = "Preferred Working Shift",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    shiftOptions.forEach { shift ->
                        val isSelected = state.shiftType == shift
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { viewModel.setShiftType(shift) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                                else Color(0xFFF8FAFC)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                if (isSelected) 1.5.dp else 1.dp,
                                if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFE2E8F0)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF64748B),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = shift,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF1E293B)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = { viewModel.nextStep() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("wizard_step1_next_btn"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Next: Vehicle & Driving License", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        // STEP 2: Vehicle & Route Preference
        if (currentStep == 2) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("2", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Vehicle & Fleet Capabilities",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Select your vehicle type and driving license status",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    Text(
                        text = "Choose your delivery vehicle type:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF334155)
                    )

                    vehicleOptions.forEach { opt ->
                        val isSelected = state.vehicleType == opt.title
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    viewModel.setVehicleType(opt.title)
                                    viewModel.updateBike(opt.hasBike)
                                }
                                .testTag("vehicle_type_${opt.title.replace(" ", "_")}"),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                else Color(0xFFF8FAFC)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                if (isSelected) 2.dp else 1.dp,
                                if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFE2E8F0)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isSelected) MaterialTheme.colorScheme.primary
                                            else Color(0xFFE2E8F0)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = opt.icon,
                                        contentDescription = opt.title,
                                        tint = if (isSelected) Color.White else Color(0xFF475569),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = opt.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF0F172A)
                                    )
                                    Text(
                                        text = opt.subtitle,
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    // Bike Available explicit toggle
                    Text(
                        text = "Two-Wheeler / Bike Available (Required)*",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { viewModel.updateBike(true) }
                                .testTag("bike_option_yes"),
                            colors = CardDefaults.cardColors(
                                containerColor = if (state.bikeAvailable) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                else Color(0xFFF8FAFC)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                if (state.bikeAvailable) MaterialTheme.colorScheme.primary else Color(0xFFE2E8F0)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsBike,
                                    contentDescription = null,
                                    tint = if (state.bikeAvailable) MaterialTheme.colorScheme.primary else Color(0xFF64748B),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Yes (Available)",
                                    fontWeight = if (state.bikeAvailable) FontWeight.Bold else FontWeight.Medium,
                                    color = if (state.bikeAvailable) MaterialTheme.colorScheme.primary else Color(0xFF475569),
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { viewModel.updateBike(false) }
                                .testTag("bike_option_no"),
                            colors = CardDefaults.cardColors(
                                containerColor = if (!state.bikeAvailable) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                else Color(0xFFF8FAFC)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                if (!state.bikeAvailable) MaterialTheme.colorScheme.primary else Color(0xFFE2E8F0)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NoTransfer,
                                    contentDescription = null,
                                    tint = if (!state.bikeAvailable) MaterialTheme.colorScheme.primary else Color(0xFF64748B),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "No (Cycle/Walk)",
                                    fontWeight = if (!state.bikeAvailable) FontWeight.Bold else FontWeight.Medium,
                                    color = if (!state.bikeAvailable) MaterialTheme.colorScheme.primary else Color(0xFF475569),
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    // Driving License toggle
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = state.hasDrivingLicense,
                                onCheckedChange = { viewModel.setDrivingLicense(it) }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "Valid Driving License Available",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                                Text(
                                    text = "Mandatory for motorized delivery riders (Motorcycle/EV)",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.prevStep() },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Back")
                        }

                        Button(
                            onClick = { viewModel.nextStep() },
                            modifier = Modifier
                                .weight(1.5f)
                                .height(48.dp)
                                .testTag("wizard_step2_next_btn"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Next: Upload KYC Docs", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        // STEP 3: Identity Documents (KYC) Upload
        if (currentStep == 3) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("3", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Identity Documents (KYC)",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Upload Aadhaar, PAN card, and selfie photo",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(
                                    if (state.uploadedDocumentsCount == 5) Color(0xFFDCFCE7)
                                    else Color(0xFFFEF3C7)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${state.uploadedDocumentsCount} / 5 Uploaded",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = if (state.uploadedDocumentsCount == 5) Color(0xFF15803D)
                                else Color(0xFFB45309)
                            )
                        }
                    }

                    LinearProgressIndicator(
                        progress = { state.progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (state.uploadedDocumentsCount == 5) Color(0xFF15803D) else MaterialTheme.colorScheme.primary,
                        trackColor = Color(0xFFE2E8F0)
                    )

                    DocumentUploadCard(
                        title = "Aadhar Card Front",
                        subtitle = "Clear picture of front side showing full name and 12-digit number",
                        selectedUri = state.aadharFrontUri,
                        onUriSelected = { viewModel.updateDocument("aadhar_front", it) },
                        onRemove = { viewModel.removeDocument("aadhar_front") },
                        onPreviewZoom = { viewModel.openZoom("Aadhar Card Front", it) }
                    )

                    DocumentUploadCard(
                        title = "Aadhar Card Back",
                        subtitle = "Clear picture of back side showing registered address and QR code",
                        selectedUri = state.aadharBackUri,
                        onUriSelected = { viewModel.updateDocument("aadhar_back", it) },
                        onRemove = { viewModel.removeDocument("aadhar_back") },
                        onPreviewZoom = { viewModel.openZoom("Aadhar Card Back", it) }
                    )

                    DocumentUploadCard(
                        title = "PAN Card Front",
                        subtitle = "Front side photo of Permanent Account Number card",
                        selectedUri = state.panFrontUri,
                        onUriSelected = { viewModel.updateDocument("pan_front", it) },
                        onRemove = { viewModel.removeDocument("pan_front") },
                        onPreviewZoom = { viewModel.openZoom("PAN Card Front", it) }
                    )

                    DocumentUploadCard(
                        title = "PAN Card Back",
                        subtitle = "Back side photo of PAN card",
                        selectedUri = state.panBackUri,
                        onUriSelected = { viewModel.updateDocument("pan_back", it) },
                        onRemove = { viewModel.removeDocument("pan_back") },
                        onPreviewZoom = { viewModel.openZoom("PAN Card Back", it) }
                    )

                    DocumentUploadCard(
                        title = "Selfie Photo",
                        subtitle = "Recent clear passport-style selfie without cap or sunglasses",
                        selectedUri = state.selfieUri,
                        onUriSelected = { viewModel.updateDocument("selfie", it) },
                        onRemove = { viewModel.removeDocument("selfie") },
                        onPreviewZoom = { viewModel.openZoom("Applicant Selfie", it) }
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.prevStep() },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Back")
                        }

                        Button(
                            onClick = { viewModel.nextStep() },
                            modifier = Modifier
                                .weight(1.5f)
                                .height(48.dp)
                                .testTag("wizard_step3_next_btn"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Review Application", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        // STEP 4: Review Application & Final Submit
        if (currentStep == 4) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("4", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Review & Submit Application",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Verify your details before final submission",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    // Application Summary Box
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "APPLICATION SUMMARY",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp,
                                color = MaterialTheme.colorScheme.primary
                            )

                            SummaryRow("Applicant Name", state.name)
                            SummaryRow("Mobile Number", "+91 ${state.mobile}")
                            SummaryRow("City Hub", state.cityHub)
                            SummaryRow("Vehicle Type", state.vehicleType)
                            SummaryRow("Bike Available", if (state.bikeAvailable) "Yes" else "No")
                            SummaryRow("Shift Preference", state.shiftType)
                            SummaryRow("Driving License", if (state.hasDrivingLicense) "Yes, Verified" else "Not Applicable")

                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                            Text(
                                text = "UPLOADED DOCUMENTS (5/5)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp,
                                color = Color(0xFF15803D)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                DocumentCheckBadge("Aadhaar", state.aadharFrontUri != null && state.aadharBackUri != null)
                                DocumentCheckBadge("PAN Card", state.panFrontUri != null && state.panBackUri != null)
                                DocumentCheckBadge("Selfie", state.selfieUri != null)
                            }
                        }
                    }

                    // Consent declaration
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = state.consentAccepted,
                                onCheckedChange = { viewModel.updateConsent(it) },
                                modifier = Modifier.testTag("consent_checkbox")
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "I declare that all submitted personal details and documents are authentic and grant consent to Ekart Logistics for delivery partner onboarding background checks.",
                                fontSize = 12.sp,
                                color = Color(0xFF334155),
                                lineHeight = 16.sp
                            )
                        }
                    }

                    // Navigation & Submit Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.prevStep() },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Back")
                        }

                        Button(
                            onClick = {
                                viewModel.submitApplication {
                                    onApplicationSubmitted()
                                }
                            },
                            enabled = !state.isSubmitting,
                            modifier = Modifier
                                .weight(2f)
                                .height(52.dp)
                                .testTag("submit_application_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            )
                        ) {
                            if (state.isSubmitting) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Submitting to Cloud DB...", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            } else {
                                Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Submit Application", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Encrypted & Secure Ekart Logistics Cloud Database",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 12.sp, color = Color(0xFF64748B))
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
    }
}

@Composable
private fun DocumentCheckBadge(name: String, isDone: Boolean) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isDone) Color(0xFFDCFCE7) else Color(0xFFFEE2E2))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isDone) Icons.Default.Check else Icons.Default.Error,
            contentDescription = null,
            tint = if (isDone) Color(0xFF15803D) else Color(0xFFDC2626),
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = name,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDone) Color(0xFF15803D) else Color(0xFFDC2626)
        )
    }
}

@Composable
private fun WizardStepPill(
    stepNumber: Int,
    label: String,
    isActive: Boolean,
    isDone: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .background(
                if (isActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                else Color.Transparent
            )
            .padding(horizontal = 6.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(
                    if (isDone) Color(0xFF15803D)
                    else if (isActive) MaterialTheme.colorScheme.primary
                    else Color(0xFFCBD5E1)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isDone) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            } else {
                Text(
                    text = "$stepNumber",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = if (isActive) Color.White else Color(0xFF475569)
                )
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            color = if (isActive) MaterialTheme.colorScheme.primary else Color(0xFF64748B)
        )
    }
}

// ---------------------------------------------------------
// 2. CLASSIC CONTINUOUS FORM (Single Scroll)
// ---------------------------------------------------------
@Composable
private fun ClassicContinuousForm(
    state: RegistrationFormState,
    vehicleOptions: List<VehicleOption>,
    cityHubs: List<String>,
    shiftOptions: List<String>,
    viewModel: DeliveryViewModel,
    onApplicationSubmitted: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Section 1
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "1. Personal & Vehicle Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = state.name,
                    onValueChange = { viewModel.updateName(it) },
                    label = { Text("Full Name (as per Aadhaar)*") },
                    placeholder = { Text("e.g. Ramesh Kumar Sharma") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_full_name"),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = state.mobile,
                    onValueChange = { viewModel.updateMobile(it) },
                    label = { Text("Mobile Number (10 Digits)*") },
                    placeholder = { Text("9876543210") },
                    leadingIcon = {
                        Row(
                            modifier = Modifier.padding(start = 12.dp, end = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "+91",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_mobile_number"),
                    shape = RoundedCornerShape(8.dp)
                )

                Text(
                    text = "Do you have a Two-Wheeler / Bike available?*",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { viewModel.updateBike(true) }
                            .testTag("bike_option_yes"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (state.bikeAvailable) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            else Color(0xFFF8FAFC)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp,
                            if (state.bikeAvailable) MaterialTheme.colorScheme.primary else Color(0xFFE2E8F0)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.DirectionsBike, contentDescription = null, tint = if (state.bikeAvailable) MaterialTheme.colorScheme.primary else Color(0xFF64748B), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Yes, Bike Available", fontWeight = if (state.bikeAvailable) FontWeight.Bold else FontWeight.Medium, fontSize = 13.sp)
                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { viewModel.updateBike(false) }
                            .testTag("bike_option_no"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (!state.bikeAvailable) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            else Color(0xFFF8FAFC)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp,
                            if (!state.bikeAvailable) MaterialTheme.colorScheme.primary else Color(0xFFE2E8F0)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.NoTransfer, contentDescription = null, tint = if (!state.bikeAvailable) MaterialTheme.colorScheme.primary else Color(0xFF64748B), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("No (Cycle / Walk)", fontWeight = if (!state.bikeAvailable) FontWeight.Bold else FontWeight.Medium, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Section 2: Documents Uploads & Submission
        DocumentsContinuousSection(
            state = state,
            viewModel = viewModel,
            onApplicationSubmitted = onApplicationSubmitted
        )
    }
}

// ---------------------------------------------------------
// 3. ACCORDION SECTIONS FORM
// ---------------------------------------------------------
@Composable
private fun AccordionSectionsForm(
    state: RegistrationFormState,
    vehicleOptions: List<VehicleOption>,
    cityHubs: List<String>,
    shiftOptions: List<String>,
    viewModel: DeliveryViewModel,
    onApplicationSubmitted: () -> Unit
) {
    var personalExpanded by remember { mutableStateOf(true) }
    var vehicleExpanded by remember { mutableStateOf(true) }
    var docsExpanded by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Module 1
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { personalExpanded = !personalExpanded },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("1. Personal Details", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    Icon(
                        imageVector = if (personalExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null
                    )
                }

                AnimatedVisibility(visible = personalExpanded) {
                    Column(
                        modifier = Modifier.padding(top = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = state.name,
                            onValueChange = { viewModel.updateName(it) },
                            label = { Text("Full Name (as per Aadhaar)") },
                            modifier = Modifier.fillMaxWidth().testTag("input_full_name")
                        )
                        OutlinedTextField(
                            value = state.mobile,
                            onValueChange = { viewModel.updateMobile(it) },
                            label = { Text("Mobile Number (10 Digits)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().testTag("input_mobile_number")
                        )
                    }
                }
            }
        }

        // Module 2
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { vehicleExpanded = !vehicleExpanded },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DirectionsBike, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("2. Vehicle Details (${if (state.bikeAvailable) "Bike: Yes" else "Bike: No"})", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    Icon(
                        imageVector = if (vehicleExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null
                    )
                }

                AnimatedVisibility(visible = vehicleExpanded) {
                    Column(
                        modifier = Modifier.padding(top = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { viewModel.updateBike(true) },
                                modifier = Modifier.weight(1f).testTag("bike_option_yes"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (state.bikeAvailable) MaterialTheme.colorScheme.primary else Color(0xFF94A3B8)
                                )
                            ) {
                                Text("Bike: Yes", fontSize = 12.sp)
                            }
                            Button(
                                onClick = { viewModel.updateBike(false) },
                                modifier = Modifier.weight(1f).testTag("bike_option_no"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (!state.bikeAvailable) MaterialTheme.colorScheme.primary else Color(0xFF94A3B8)
                                )
                            ) {
                                Text("Bike: No", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Module 3
        DocumentsContinuousSection(
            state = state,
            viewModel = viewModel,
            onApplicationSubmitted = onApplicationSubmitted
        )
    }
}

// ---------------------------------------------------------
// CONTINUOUS FORM DOCUMENTS & SUBMIT SECTION
// ---------------------------------------------------------
@Composable
private fun DocumentsContinuousSection(
    state: RegistrationFormState,
    viewModel: DeliveryViewModel,
    onApplicationSubmitted: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "2. Required Identity Documents",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Accepted formats: JPG, JPEG, PNG (Max 5MB)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (state.uploadedDocumentsCount == 5) Color(0xFFDCFCE7)
                            else Color(0xFFFEF3C7)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${state.uploadedDocumentsCount} / 5 Done",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = if (state.uploadedDocumentsCount == 5) Color(0xFF15803D)
                        else Color(0xFFB45309)
                    )
                }
            }

            LinearProgressIndicator(
                progress = { state.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (state.uploadedDocumentsCount == 5) Color(0xFF15803D) else MaterialTheme.colorScheme.primary,
                trackColor = Color(0xFFE2E8F0)
            )

            DocumentUploadCard(
                title = "Aadhar Card Front",
                subtitle = "Clear picture of front side showing full name and 12-digit number",
                selectedUri = state.aadharFrontUri,
                onUriSelected = { viewModel.updateDocument("aadhar_front", it) },
                onRemove = { viewModel.removeDocument("aadhar_front") },
                onPreviewZoom = { viewModel.openZoom("Aadhar Card Front", it) }
            )

            DocumentUploadCard(
                title = "Aadhar Card Back",
                subtitle = "Clear picture of back side showing registered address and QR code",
                selectedUri = state.aadharBackUri,
                onUriSelected = { viewModel.updateDocument("aadhar_back", it) },
                onRemove = { viewModel.removeDocument("aadhar_back") },
                onPreviewZoom = { viewModel.openZoom("Aadhar Card Back", it) }
            )

            DocumentUploadCard(
                title = "PAN Card Front",
                subtitle = "Front side photo of Permanent Account Number card",
                selectedUri = state.panFrontUri,
                onUriSelected = { viewModel.updateDocument("pan_front", it) },
                onRemove = { viewModel.removeDocument("pan_front") },
                onPreviewZoom = { viewModel.openZoom("PAN Card Front", it) }
            )

            DocumentUploadCard(
                title = "PAN Card Back",
                subtitle = "Back side photo of PAN card",
                selectedUri = state.panBackUri,
                onUriSelected = { viewModel.updateDocument("pan_back", it) },
                onRemove = { viewModel.removeDocument("pan_back") },
                onPreviewZoom = { viewModel.openZoom("PAN Card Back", it) }
            )

            DocumentUploadCard(
                title = "Selfie Photo",
                subtitle = "Recent clear passport-style selfie without cap or sunglasses",
                selectedUri = state.selfieUri,
                onUriSelected = { viewModel.updateDocument("selfie", it) },
                onRemove = { viewModel.removeDocument("selfie") },
                onPreviewZoom = { viewModel.openZoom("Applicant Selfie", it) }
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.consentAccepted,
                        onCheckedChange = { viewModel.updateConsent(it) },
                        modifier = Modifier.testTag("consent_checkbox")
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "I declare that all submitted personal details and documents are authentic and grant consent to Ekart Logistics for delivery partner onboarding background checks.",
                        fontSize = 12.sp,
                        color = Color(0xFF334155),
                        lineHeight = 16.sp
                    )
                }
            }

            Button(
                onClick = {
                    viewModel.submitApplication {
                        onApplicationSubmitted()
                    }
                },
                enabled = !state.isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_application_button"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                if (state.isSubmitting) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Submitting to Cloud DB...", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                } else {
                    Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submit Application", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
