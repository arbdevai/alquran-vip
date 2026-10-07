package com.arbdevai.quranvip.ui.theme

import androidx.compose.ui.graphics.Color

// A warm, restrained night palette. Black is reserved for the canvas; contrast is created
// with quiet tonal layers rather than bright borders or repeated gradients.
val BgCanvas = Color(0xFF0B0C0E)
val SurfaceCard = Color(0xFF141518)
val SurfaceRaised = Color(0xFF1A1C20)
val SurfaceInput = Color(0xFF1C1E22)
val SurfacePill = Color(0xFF25272C)
val BorderSubtle = Color(0xFF2A2C31)

// One accent, used intentionally: navigation selection, primary action, and current state.
val AmberAccent = Color(0xFFE5C378)
val AmberPressed = Color(0xFFCDAA5F)
val EmeraldGradientStart = Color(0xFF225F4C)
val EmeraldGradientEnd = Color(0xFF183D35)

val TextPrimary = Color(0xFFF4F1EA)
val TextTranslation = Color(0xFFD1D4DA)
val TextSecondary = Color(0xFF9CA1AA)
val TextTransliteration = Color(0xFFDCC99A)
val TextPlaceholder = Color(0xFF70757E)

val CatQuran = Color(0xFF88AEEA)
val CatJadwal = Color(0xFF75BDA2)
val CatTasbih = Color(0xFFE2A365)
val CatTahlil = Color(0xFFD89DB6)
val CatDoa = Color(0xFF72BDB5)
val CatHadroh = Color(0xFF9EA9E6)

// Legacy names retained for component compatibility. They now express a quiet tonal layer,
// not high-gloss glass decoration.
val GlassBackground = Color(0xE00B0C0E)
val GlassSurface = SurfaceCard
val GlassNavbar = Color(0xF0141518)
val GlassBorder = BorderSubtle
val GlassBorderGlow = AmberAccent.copy(alpha = 0.32f)
val GlassHighlight = Color.White.copy(alpha = 0.04f)
val GlassCardPressed = Color(0xFF222016)
