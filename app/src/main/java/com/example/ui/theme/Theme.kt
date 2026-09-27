package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

enum class UiDesignTheme(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val primaryColor: Color,
    val secondaryColor: Color
) {
    EKART_CLASSIC(
        id = "ekart_classic",
        displayName = "Ekart Royal Blue",
        subtitle = "Official Logistics (Blue & Amber)",
        primaryColor = EkartBluePrimary,
        secondaryColor = EkartYellow
    ),
    ECO_FLEET(
        id = "eco_fleet",
        displayName = "Eco Green Fleet",
        subtitle = "Electric & Sustainable (Emerald)",
        primaryColor = EcoEmeraldPrimary,
        secondaryColor = EcoEmeraldAccent
    ),
    EXPRESS_CYBER(
        id = "express_cyber",
        displayName = "Cyber Express",
        subtitle = "Fast Night Delivery (Indigo & Violet)",
        primaryColor = CyberPurplePrimary,
        secondaryColor = CyberPurpleAccent
    ),
    SUNRISE_ORANGE(
        id = "sunrise_orange",
        displayName = "Hyperlocal Sunrise",
        subtitle = "10-Min Quick Commerce (Orange & Gold)",
        primaryColor = SunriseOrangePrimary,
        secondaryColor = SunriseOrangeAccent
    ),
    DARK_COMMAND(
        id = "dark_command",
        displayName = "Midnight Command",
        subtitle = "OLED Dark Logistics Terminal",
        primaryColor = DarkSlatePrimary,
        secondaryColor = DarkSlateAccent
    )
}

enum class UiLayoutType(val id: String, val title: String, val description: String) {
    STEPPER_WIZARD(
        id = "wizard",
        title = "3-Step Smart Wizard",
        description = "Guided progressive steps (Profile → Vehicle → KYC Docs)"
    ),
    CLASSIC_ALL_IN_ONE(
        id = "classic",
        title = "Continuous Form",
        description = "All fields in a streamlined single-scroll layout"
    ),
    EXPANDABLE_SECTIONS(
        id = "cards",
        title = "Modular Cards",
        description = "Collapsible interactive card modules"
    )
}

enum class DashboardViewType(val id: String, val label: String) {
    CARDS("cards", "Modern Cards"),
    TABLE("table", "Data Table")
}

private val EkartLightColorScheme = lightColorScheme(
    primary = EkartBluePrimary,
    onPrimary = Color.White,
    primaryContainer = EkartBlueContainer,
    onPrimaryContainer = EkartOnBlueContainer,
    secondary = EkartYellow,
    onSecondary = EkartOnYellow,
    secondaryContainer = EkartYellowLight,
    onSecondaryContainer = EkartYellowDark,
    background = SurfaceLight,
    surface = SurfaceCard,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextSecondary,
    outline = DividerColor
)

private val EcoLightColorScheme = lightColorScheme(
    primary = EcoEmeraldPrimary,
    onPrimary = Color.White,
    primaryContainer = EcoEmeraldContainer,
    onPrimaryContainer = EcoEmeraldDark,
    secondary = EcoEmeraldAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE6FFFA),
    onSecondaryContainer = EcoEmeraldDark,
    background = Color(0xFFF0FDF4),
    surface = SurfaceCard,
    onBackground = Color(0xFF064E3B),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE7F9EE),
    onSurfaceVariant = Color(0xFF047857),
    outline = Color(0xFFA7F3D0)
)

private val CyberLightColorScheme = lightColorScheme(
    primary = CyberPurplePrimary,
    onPrimary = Color.White,
    primaryContainer = CyberPurpleContainer,
    onPrimaryContainer = CyberPurpleDark,
    secondary = CyberPurpleAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEEF2FF),
    onSecondaryContainer = CyberPurpleDark,
    background = Color(0xFFF5F3FF),
    surface = SurfaceCard,
    onBackground = Color(0xFF1E1B4B),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFEDE9FE),
    onSurfaceVariant = Color(0xFF4338CA),
    outline = Color(0xFFC7D2FE)
)

private val SunriseLightColorScheme = lightColorScheme(
    primary = SunriseOrangePrimary,
    onPrimary = Color.White,
    primaryContainer = SunriseOrangeContainer,
    onPrimaryContainer = SunriseOrangeDark,
    secondary = SunriseOrangeAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFF7ED),
    onSecondaryContainer = SunriseOrangeDark,
    background = Color(0xFFFFFBF5),
    surface = SurfaceCard,
    onBackground = Color(0xFF431407),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFFFEDD5),
    onSurfaceVariant = Color(0xFFC2410C),
    outline = Color(0xFFFED7AA)
)

private val MidnightDarkColorScheme = darkColorScheme(
    primary = DarkSlatePrimary,
    onPrimary = Color(0xFF0F172A),
    primaryContainer = DarkSlateContainer,
    onPrimaryContainer = Color(0xFFBAE6FD),
    secondary = DarkSlateAccent,
    onSecondary = Color(0xFF0F172A),
    background = Color(0xFF0B1120),
    surface = Color(0xFF1E293B),
    onBackground = Color(0xFFF8FAFC),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF475569)
)

private val DefaultDarkColorScheme = darkColorScheme(
    primary = EkartBlueLight,
    onPrimary = Color.White,
    primaryContainer = EkartBlueDark,
    onPrimaryContainer = EkartBlueContainer,
    secondary = EkartYellow,
    onSecondary = EkartOnYellow,
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    onBackground = Color(0xFFF8FAFC),
    onSurface = Color(0xFFF8FAFC),
)

@Composable
fun MyApplicationTheme(
    designTheme: UiDesignTheme = UiDesignTheme.EKART_CLASSIC,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (designTheme == UiDesignTheme.DARK_COMMAND) {
        MidnightDarkColorScheme
    } else if (darkTheme) {
        DefaultDarkColorScheme
    } else {
        when (designTheme) {
            UiDesignTheme.EKART_CLASSIC -> EkartLightColorScheme
            UiDesignTheme.ECO_FLEET -> EcoLightColorScheme
            UiDesignTheme.EXPRESS_CYBER -> CyberLightColorScheme
            UiDesignTheme.SUNRISE_ORANGE -> SunriseLightColorScheme
            UiDesignTheme.DARK_COMMAND -> MidnightDarkColorScheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
