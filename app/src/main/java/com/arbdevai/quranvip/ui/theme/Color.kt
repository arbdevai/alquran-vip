package com.arbdevai.quranvip.ui.theme

import androidx.compose.ui.graphics.Color

// ==========================================
// 1. Root Canvas & Rich AMOLED Dark Surfaces
// ==========================================
val BgCanvas = Color(0xFF000000)             // Pure pitch black AMOLED
val SurfaceCard = Color(0xFF14171E)          // Deep slate card surface
val SurfaceRaised = Color(0xFF1C2028)        // Raised surface for dialogs & popups
val SurfaceInput = Color(0xFF181B22)         // Search bar & input fields
val SurfacePill = Color(0xFF222631)          // Pill badges & chips
val BorderSubtle = Color(0x1FFFFFFF)         // 12% white outline border

// ==========================================
// 2. Signature Emerald-Teal Identity (Yasin)
// ==========================================
val EmeraldGradientStart = Color(0xFF075E54) // Deep Islamic green
val EmeraldGradientMid = Color(0xFF087F67)   // Vibrant emerald teal
val EmeraldGradientEnd = Color(0xFF10A37F)   // Fresh luminous mint

// Warm Gold / Amber Accent for active states & highlights
val AmberAccent = Color(0xFFFFB020)          // Luminous warm amber gold
val AmberPressed = Color(0xFFE59807)         // Pressed state
val AmberSoft = Color(0x2BFFB020)            // Glow / background wash

// ==========================================
// 3. Typographic Text Hierarchy
// ==========================================
val TextPrimary = Color(0xFFFFFFFF)          // Pure white for Arabic verses & titles
val TextTransliteration = Color(0xFFFFB020)  // Warm golden transliteration
val TextTranslation = Color(0xFFCBD5E1)      // Crisp silver-gray for Indonesian translation
val TextSecondary = Color(0xFF94A3B8)        // Slate metadata & captions
val TextPlaceholder = Color(0xFF64748B)      // Search hints

// ==========================================
// 4. Feature Tile Icon Gradients (Yasin Style)
// ==========================================
val TileQuranStart = Color(0xFF0A84FF)
val TileQuranEnd = Color(0xFF5AC8FA)

val TileJadwalStart = Color(0xFF30D158)
val TileJadwalEnd = Color(0xFF00C7BE)

val TileTasbihStart = Color(0xFFFF9F0A)
val TileTasbihEnd = Color(0xFFFF453A)

val TileBookmarkStart = Color(0xFFFF375F)
val TileBookmarkEnd = Color(0xFFBF5AF2)

val TileSettingsStart = Color(0xFF5E5CE6)
val TileSettingsEnd = Color(0xFF64D2FF)

// Category colors for backwards compatibility
val CatQuran = Color(0xFF0A84FF)
val CatJadwal = Color(0xFF30D158)
val CatTasbih = Color(0xFFFF9F0A)
val CatTahlil = Color(0xFFFF375F)
val CatDoa = Color(0xFF00C7BE)
val CatHadroh = Color(0xFF5E5CE6)

// ==========================================
// 5. Modern Translucent Dock & Card Tokens
// ==========================================
val GlassBackground = Color(0xB8121418)
val GlassSurface = Color(0xDB161A22)          // Rich translucent card
val GlassNavbar = Color(0xF2141720)           // Floating island navigation bar
val GlassBorder = Color(0x29FFFFFF)           // Specular rim border (16% white)
val GlassBorderGlow = Color(0x52FFB020)       // Warm gold active glow
val GlassHighlight = Color(0x1FFFFFFF)
val GlassCardPressed = Color(0xEE1E232F)
