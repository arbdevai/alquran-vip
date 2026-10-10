package com.arbdevai.quranvip.ui.theme

import androidx.compose.ui.graphics.Color

// ==========================================
// 1. Root Canvas & Bento Grid Surfaces (Fresh White & Blue)
// ==========================================
val BgCanvas = Color(0xFFF5F9FF)             // Very light cool blue background (Fresh)
val SurfaceCard = Color(0xFFFFFFFF)          // Pure White Bento Boxes
val SurfaceRaised = Color(0xFFFFFFFF)        // Dialogs
val SurfaceInput = Color(0xFFEAF1FA)         // Search bar & inputs
val SurfacePill = Color(0xFFE0EDF8)          // Pills & chips
val BorderSubtle = Color(0xFFD6E4F0)         // Clean subtle border for Bento Grid

// ==========================================
// 2. Fresh Blue Identity
// ==========================================
// Primary Accent: Fresh Blue
val AmberAccent = Color(0xFF2563EB)          // Royal Blue Accent (kept variable name AmberAccent for compatibility)
val AmberPressed = Color(0xFF1D4ED8)         // Darker Blue Pressed
val AmberSoft = Color(0x262563EB)            // Light blue background wash

// Hero Banner Gradient (Fresh Blue Gradient)
val EmeraldGradientStart = Color(0xFF60A5FA) // Light Blue
val EmeraldGradientMid = Color(0xFF3B82F6)   // Medium Blue
val EmeraldGradientEnd = Color(0xFF2563EB)   // Deep Blue

// ==========================================
// 3. Typographic Text Hierarchy
// ==========================================
val TextPrimary = Color(0xFF1E293B)          // Dark Slate for primary text
val TextTransliteration = Color(0xFF2563EB)  // Blue for transliteration
val TextTranslation = Color(0xFF475569)      // Slate gray for translation
val TextSecondary = Color(0xFF64748B)        // Light slate gray for captions
val TextPlaceholder = Color(0xFF94A3B8)      // Search hints

// ==========================================
// 4. Feature Tile / Bento Icon Colors
// ==========================================
val TileQuranStart = Color(0xFF3B82F6)       // Solid Blue
val TileQuranEnd = Color(0xFF3B82F6)

val TileJadwalStart = Color(0xFF0EA5E9)      // Sky Blue
val TileJadwalEnd = Color(0xFF0EA5E9)

val TileTasbihStart = Color(0xFF8B5CF6)      // Violet/Purple Accent
val TileTasbihEnd = Color(0xFF8B5CF6)

val TileBookmarkStart = Color(0xFFF59E0B)    // Amber for Bookmark
val TileBookmarkEnd = Color(0xFFF59E0B)

val TileSettingsStart = Color(0xFF64748B)    // Slate
val TileSettingsEnd = Color(0xFF64748B)

val CatJadwal = Color(0xFF0EA5E9)

// ==========================================
// 5. Solid / Flat Fallbacks (Bento Style)
// ==========================================
val GlassBackground = SurfaceCard
val GlassSurface = SurfaceCard
val GlassNavbar = Color(0xFFFFFFFF)           // Solid white navbar
val GlassBorder = BorderSubtle
val GlassBorderGlow = AmberAccent
val GlassHighlight = Color.Transparent
val GlassCardPressed = Color(0xFFF0F6FF)      // Light blue tint on press
