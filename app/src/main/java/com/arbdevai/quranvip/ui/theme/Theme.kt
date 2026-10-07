package com.arbdevai.quranvip.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val QuranColorScheme = darkColorScheme(
    primary = AmberAccent,
    onPrimary = Color(0xFF141518),
    primaryContainer = SurfacePill,
    onPrimaryContainer = TextPrimary,
    secondary = EmeraldGradientStart,
    onSecondary = Color.White,
    background = BgCanvas,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceInput,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    error = Color(0xFFE57373)
)

@Composable
fun QuranVipTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = QuranColorScheme,
        typography = QuranTypography,
        shapes = QuranShapes,
        content = content
    )
}
