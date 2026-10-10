package com.arbdevai.quranvip.ui.theme

import androidx.compose.ui.graphics.Color

// ==========================================
// 1. Root Canvas & Bento Grid Surfaces (Desert Sand)
// ==========================================
val BgCanvas = Color(0xFFF8F5F0)             // Desert Sand Background (Warm & easy on eyes)
val SurfaceCard = Color(0xFFFFFFFF)          // Pure White Bento Boxes
val SurfaceRaised = Color(0xFFFFFFFF)        // Dialogs
val SurfaceInput = Color(0xFFF0ECE4)         // Search bar & inputs
val SurfacePill = Color(0xFFEBE6DD)          // Pills & chips
val BorderSubtle = Color(0xFFE5DFD5)         // Clean subtle border for Bento Grid

// ==========================================
// 2. Copper & Emerald Identity
// ==========================================
// Primary Accent: Warm Copper
val AmberAccent = Color(0xFFC67C4E)          // Copper Accent (replaces Amber)
val AmberPressed = Color(0xFFA6643B)         // Copper Pressed
val AmberSoft = Color(0x26C67C4E)            // Copper background wash

// Hero Banner Gradient (Optional use for Hero Cards)
val EmeraldGradientStart = Color(0xFFDCA983) // Soft Copper
val EmeraldGradientMid = Color(0xFFC67C4E)   // Base Copper
val EmeraldGradientEnd = Color(0xFFB0643A)   // Deep Copper

// ==========================================
// 3. Typographic Text Hierarchy
// ==========================================
val TextPrimary = Color(0xFF2D2A26)          // Espresso Dark for primary text
val TextTransliteration = Color(0xFFC67C4E)  // Copper for transliteration
val TextTranslation = Color(0xFF5A554E)      // Medium warm gray for translation
val TextSecondary = Color(0xFF8A857C)        // Light warm gray for captions
val TextPlaceholder = Color(0xFFA19C92)      // Search hints

// ==========================================
// 4. Feature Tile / Bento Icon Colors
// ==========================================
// In Bento Grid, we avoid gradients and use solid, soft elegant colors
val TileQuranStart = Color(0xFF507871)       // Deep Muted Emerald
val TileQuranEnd = Color(0xFF507871)

val TileJadwalStart = Color(0xFFB28C5E)      // Desert Gold
val TileJadwalEnd = Color(0xFFB28C5E)

val TileTasbihStart = Color(0xFFC67C4E)      // Copper
val TileTasbihEnd = Color(0xFFC67C4E)

val TileBookmarkStart = Color(0xFF886358)    // Warm Terracotta
val TileBookmarkEnd = Color(0xFF886358)

val TileSettingsStart = Color(0xFF5B6973)    // Slate Blue-Gray
val TileSettingsEnd = Color(0xFF5B6973)

val CatJadwal = Color(0xFFB28C5E)

// ==========================================
// 5. Solid / Flat Fallbacks (Removing Glassmorphism)
// ==========================================
val GlassBackground = SurfaceCard
val GlassSurface = SurfaceCard
val GlassNavbar = Color(0xFFFFFFFF)           // Solid white navbar
val GlassBorder = BorderSubtle
val GlassBorderGlow = AmberAccent
val GlassHighlight = Color.Transparent
val GlassCardPressed = Color(0xFFF7F4F0)
