package com.arbdevai.quranvip.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.compositionLocalOf

val LocalThemeIsDark = compositionLocalOf { false }

@Composable
fun QuranVipTheme(content: @Composable () -> Unit) {
    val isDark = LocalThemeIsDark.current
    val colorScheme = if (isDark) {
        darkColorScheme(
            primary = AmberAccent,
            onPrimary = OnAmberAccent,
            primaryContainer = AmberSoft,
            onPrimaryContainer = AmberAccent,
            secondary = AmberAccent,
            onSecondary = OnAmberAccent,
            background = BgCanvas,
            onBackground = TextPrimary,
            surface = SurfaceCard,
            onSurface = TextPrimary,
            surfaceVariant = SurfaceInput,
            onSurfaceVariant = TextSecondary,
            outline = BorderSubtle,
            error = Color(0xFFE57373)
        )
    } else {
        lightColorScheme(
            primary = AmberAccent,
            onPrimary = OnAmberAccent,
            primaryContainer = AmberSoft,
            onPrimaryContainer = AmberAccent,
            secondary = AmberAccent,
            onSecondary = OnAmberAccent,
            background = BgCanvas,
            onBackground = TextPrimary,
            surface = SurfaceCard,
            onSurface = TextPrimary,
            surfaceVariant = SurfaceInput,
            onSurfaceVariant = TextSecondary,
            outline = BorderSubtle,
            error = Color(0xFFE57373)
        )
    }
    
    val view = androidx.compose.ui.platform.LocalView.current
    if (!view.isInEditMode) {
        androidx.compose.runtime.SideEffect {
            val window = (view.context as android.app.Activity).window
            androidx.core.view.WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !isDark
                isAppearanceLightNavigationBars = !isDark
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = QuranTypography,
        shapes = QuranShapes,
        content = content
    )
}
