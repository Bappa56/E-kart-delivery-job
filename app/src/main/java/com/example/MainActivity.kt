package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.HowToReg
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.DeliveryApplication
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.IconButton
import com.example.ui.components.ImageZoomDialog
import com.example.ui.components.UiDesignSettingsDialog
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AdminLoginScreen
import com.example.ui.screens.ApplicationDetailScreen
import com.example.ui.screens.RegistrationScreen
import com.example.ui.screens.StatusCheckScreen
import com.example.ui.screens.SubmissionSuccessScreen
import com.example.ui.theme.EkartBlueDark
import com.example.ui.theme.EkartYellow
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.DeliveryViewModel

enum class EkartScreen {
    REGISTER,
    SUCCESS,
    TRACK_STATUS,
    ADMIN
}

class MainActivity : ComponentActivity() {

    private val viewModel: DeliveryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentUiTheme by viewModel.currentUiTheme.collectAsStateWithLifecycle()
            MyApplicationTheme(designTheme = currentUiTheme) {
                EkartApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun EkartApp(viewModel: DeliveryViewModel) {
    var currentScreen by remember { mutableStateOf(EkartScreen.REGISTER) }
    var submittedApp by remember { mutableStateOf<DeliveryApplication?>(null) }
    var viewingAdminApp by remember { mutableStateOf<DeliveryApplication?>(null) }

    val adminAuth by viewModel.adminAuth.collectAsStateWithLifecycle()
    val zoomPreview by viewModel.zoomPreview.collectAsStateWithLifecycle()
    val pendingCount by viewModel.pendingCount.collectAsStateWithLifecycle()
    val currentUiTheme by viewModel.currentUiTheme.collectAsStateWithLifecycle()
    val currentLayoutType by viewModel.currentLayoutType.collectAsStateWithLifecycle()
    val dashboardViewType by viewModel.dashboardViewType.collectAsStateWithLifecycle()
    val showDesignSheet by viewModel.showDesignSheet.collectAsStateWithLifecycle()

    BackHandler(enabled = viewingAdminApp != null || currentScreen != EkartScreen.REGISTER) {
        if (viewingAdminApp != null) {
            viewingAdminApp = null
        } else if (currentScreen != EkartScreen.REGISTER) {
            currentScreen = EkartScreen.REGISTER
        }
    }

    if (showDesignSheet) {
        UiDesignSettingsDialog(
            selectedTheme = currentUiTheme,
            selectedLayout = currentLayoutType,
            selectedDashboardView = dashboardViewType,
            onThemeSelected = { viewModel.setUiDesignTheme(it) },
            onLayoutSelected = { viewModel.setUiLayoutType(it) },
            onDashboardViewSelected = { viewModel.setDashboardViewType(it) },
            onDismiss = { viewModel.toggleDesignSheet(false) }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentScreen != EkartScreen.ADMIN) {
                EkartTopAppBar(onOpenDesignSettings = { viewModel.toggleDesignSheet(true) })
            }
        },
        bottomBar = {
            EkartBottomNavBar(
                currentScreen = currentScreen,
                pendingCount = pendingCount,
                onScreenSelected = { screen ->
                    if (screen == EkartScreen.REGISTER) {
                        submittedApp = null
                    }
                    viewingAdminApp = null
                    currentScreen = screen
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                EkartScreen.REGISTER -> {
                    RegistrationScreen(
                        viewModel = viewModel,
                        onApplicationSubmitted = {
                            submittedApp = viewModel.registrationState.value.successApplication
                            currentScreen = EkartScreen.SUCCESS
                        }
                    )
                }

                EkartScreen.SUCCESS -> {
                    submittedApp?.let { app ->
                        SubmissionSuccessScreen(
                            application = app,
                            onTrackStatus = {
                                viewModel.updateStatusQuery(app.applicationId)
                                viewModel.searchStatus()
                                currentScreen = EkartScreen.TRACK_STATUS
                            },
                            onNewRegistration = {
                                viewModel.resetRegistrationForm()
                                submittedApp = null
                                currentScreen = EkartScreen.REGISTER
                            }
                        )
                    } ?: run {
                        RegistrationScreen(
                            viewModel = viewModel,
                            onApplicationSubmitted = {
                                submittedApp = viewModel.registrationState.value.successApplication
                                currentScreen = EkartScreen.SUCCESS
                            }
                        )
                    }
                }

                EkartScreen.TRACK_STATUS -> {
                    StatusCheckScreen(
                        viewModel = viewModel,
                        onApplyNew = {
                            viewModel.resetRegistrationForm()
                            currentScreen = EkartScreen.REGISTER
                        }
                    )
                }

                EkartScreen.ADMIN -> {
                    if (!adminAuth.isAuthenticated) {
                        AdminLoginScreen(
                            viewModel = viewModel,
                            onLoginSuccess = {
                            }
                        )
                    } else if (viewingAdminApp != null) {
                        val currentApp = viewingAdminApp!!
                        ApplicationDetailScreen(
                            application = currentApp,
                            viewModel = viewModel,
                            onBack = { viewingAdminApp = null }
                        )
                    } else {
                        AdminDashboardScreen(
                            viewModel = viewModel,
                            onViewDetails = { app ->
                                viewingAdminApp = app
                            },
                            onLogout = {
                                viewingAdminApp = null
                            }
                        )
                    }
                }
            }

            zoomPreview?.let { (title, uriOrPath) ->
                ImageZoomDialog(
                    title = title,
                    imageUriOrPath = uriOrPath,
                    onDismiss = { viewModel.closeZoom() }
                )
            }
        }
    }
}

@Composable
fun EkartTopAppBar(onOpenDesignSettings: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(EkartBlueDark)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(EkartYellow),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalShipping,
                    contentDescription = "Ekart Logistics",
                    tint = EkartBlueDark,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "EKART",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "LOGISTICS",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = EkartYellow
                    )
                }
                Text(
                    text = "Delivery Partner Recruitment & Training",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "CLOUD DB",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = EkartYellow
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onOpenDesignSettings,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f))
                    .testTag("top_bar_ui_design_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = "Set UI Design Style Type",
                    tint = EkartYellow,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun EkartBottomNavBar(
    currentScreen: EkartScreen,
    pendingCount: Int,
    onScreenSelected: (EkartScreen) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = Color.White,
        tonalElevation = 6.dp
    ) {
        NavigationBarItem(
            selected = currentScreen == EkartScreen.REGISTER || currentScreen == EkartScreen.SUCCESS,
            onClick = { onScreenSelected(EkartScreen.REGISTER) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == EkartScreen.REGISTER) Icons.Filled.HowToReg else Icons.Outlined.HowToReg,
                    contentDescription = "Register"
                )
            },
            label = { Text("Register", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EkartBlueDark,
                selectedTextColor = EkartBlueDark,
                indicatorColor = EkartYellow.copy(alpha = 0.35f),
                unselectedIconColor = Color(0xFF64748B),
                unselectedTextColor = Color(0xFF64748B)
            ),
            modifier = Modifier.testTag("nav_register")
        )

        NavigationBarItem(
            selected = currentScreen == EkartScreen.TRACK_STATUS,
            onClick = { onScreenSelected(EkartScreen.TRACK_STATUS) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == EkartScreen.TRACK_STATUS) Icons.Filled.Search else Icons.Outlined.Search,
                    contentDescription = "Track Status"
                )
            },
            label = { Text("Track Status", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EkartBlueDark,
                selectedTextColor = EkartBlueDark,
                indicatorColor = EkartYellow.copy(alpha = 0.35f),
                unselectedIconColor = Color(0xFF64748B),
                unselectedTextColor = Color(0xFF64748B)
            ),
            modifier = Modifier.testTag("nav_track_status")
        )

        NavigationBarItem(
            selected = currentScreen == EkartScreen.ADMIN,
            onClick = { onScreenSelected(EkartScreen.ADMIN) },
            icon = {
                BadgedBox(
                    badge = {
                        if (pendingCount > 0) {
                            Badge(
                                containerColor = EkartYellow,
                                contentColor = EkartBlueDark
                            ) {
                                Text("$pendingCount", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (currentScreen == EkartScreen.ADMIN) Icons.Filled.AdminPanelSettings else Icons.Outlined.AdminPanelSettings,
                        contentDescription = "Admin Portal"
                    )
                }
            },
            label = { Text("Admin Portal", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EkartBlueDark,
                selectedTextColor = EkartBlueDark,
                indicatorColor = EkartYellow.copy(alpha = 0.35f),
                unselectedIconColor = Color(0xFF64748B),
                unselectedTextColor = Color(0xFF64748B)
            ),
            modifier = Modifier.testTag("nav_admin_portal")
        )
    }
}
