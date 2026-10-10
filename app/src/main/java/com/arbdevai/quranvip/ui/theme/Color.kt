package com.arbdevai.quranvip.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ==========================================
// 1. Root Canvas & Surfaces (Ultra Clean Minimalist)
// ==========================================
val BgCanvas: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF0F172A) else Color(0xFFF8FAFC)
val SurfaceCard: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF1E293B) else Color(0xFFFFFFFF)
val SurfaceRaised: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF1E293B) else Color(0xFFFFFFFF)
val SurfaceInput: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF334155) else Color(0xFFF1F5F9)
val SurfacePill: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF334155) else Color(0xFFF1F5F9)
val BorderSubtle: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF334155) else Color(0xFFE2E8F0)

// ==========================================
// 2. Identity (Monochrome Slate)
// ==========================================
val AmberAccent: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFFFFFFFF) else Color(0xFF0F172A)
val AmberPressed: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFFF1F5F9) else Color(0xFF334155)
val AmberSoft: Color @Composable get() = if (LocalThemeIsDark.current) Color(0x33FFFFFF) else Color(0x1A0F172A)

val EmeraldGradientStart: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF475569) else Color(0xFF334155)
val EmeraldGradientMid: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF334155) else Color(0xFF1E293B)
val EmeraldGradientEnd: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF1E293B) else Color(0xFF0F172A)

// ==========================================
// 3. Typography
// ==========================================
val TextPrimary: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFFF8FAFC) else Color(0xFF0F172A)
val TextTransliteration: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFFCBD5E1) else Color(0xFF475569)
val TextTranslation: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF94A3B8) else Color(0xFF64748B)
val TextSecondary: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF64748B) else Color(0xFF94A3B8)
val TextPlaceholder: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF475569) else Color(0xFFCBD5E1)

// ==========================================
// 4. Feature Tiles (Subtle Pastel Muted)
// ==========================================
val TileQuranStart: Color @Composable get() = EmeraldGradientStart
val TileQuranEnd: Color @Composable get() = EmeraldGradientEnd
val TileJadwalStart: Color @Composable get() = EmeraldGradientStart
val TileJadwalEnd: Color @Composable get() = EmeraldGradientEnd
val TileTasbihStart: Color @Composable get() = EmeraldGradientStart
val TileTasbihEnd: Color @Composable get() = EmeraldGradientEnd
val TileBookmarkStart: Color @Composable get() = EmeraldGradientStart
val TileBookmarkEnd: Color @Composable get() = EmeraldGradientEnd
val TileSettingsStart: Color @Composable get() = EmeraldGradientStart
val TileSettingsEnd: Color @Composable get() = EmeraldGradientEnd

val CatJadwal: Color @Composable get() = AmberAccent

val GlassBackground: Color @Composable get() = SurfaceCard
val GlassSurface: Color @Composable get() = SurfaceCard
val GlassNavbar: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF1E293B) else Color(0xFFFFFFFF)
val GlassBorder: Color @Composable get() = BorderSubtle
val GlassBorderGlow: Color @Composable get() = AmberAccent
val GlassHighlight: Color @Composable get() = Color.Transparent
val GlassCardPressed: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF334155) else Color(0xFFF8FAFC)
