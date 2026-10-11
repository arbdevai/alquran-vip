package com.arbdevai.quranvip.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ==========================================
// 1. Root Canvas & Surfaces (Corporate Indigo Clean)
// ==========================================
val BgCanvas: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF0B0F19) else Color(0xFFF8F9FE)
val SurfaceCard: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF131B2E) else Color(0xFFFFFFFF)
val SurfaceRaised: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF1A233A) else Color(0xFFFFFFFF)
val SurfaceInput: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF1E2842) else Color(0xFFEEF1FB)
val SurfacePill: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF1E2842) else Color(0xFFEEF1FB)
val BorderSubtle: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF242E48) else Color(0xFFE2E6F5)

// ==========================================
// 2. Identity (Dominant Electric Royal Indigo #5855EB)
// ==========================================
val AmberAccent: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF6E6BF0) else Color(0xFF5855EB)
val AmberPressed: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF5855EB) else Color(0xFF4542D9)
val AmberSoft: Color @Composable get() = if (LocalThemeIsDark.current) Color(0x336E6BF0) else Color(0x1F5855EB)
val OnAmberAccent: Color @Composable get() = Color.White

val EmeraldGradientStart: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF4F46E5) else Color(0xFF5855EB)
val EmeraldGradientMid: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF3730A3) else Color(0xFF4338CA)
val EmeraldGradientEnd: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF1E1B4B) else Color(0xFF312E81)

// ==========================================
// 3. Typography
// ==========================================
val TextPrimary: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFFF8FAFC) else Color(0xFF0F172A)
val TextTransliteration: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFFCBD5E1) else Color(0xFF475569)
val TextTranslation: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF94A3B8) else Color(0xFF64748B)
val TextSecondary: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF74829C) else Color(0xFF8A94A6)
val TextPlaceholder: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF475569) else Color(0xFFCBD5E1)

// ==========================================
// 4. Feature Tiles (#5855EB Harmonic Gradient Family)
// ==========================================
val TileQuranStart: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF6366F1) else Color(0xFF5855EB)
val TileQuranEnd: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF4338CA) else Color(0xFF3730A3)
val TileJadwalStart: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF818CF8) else Color(0xFF6366F1)
val TileJadwalEnd: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF4F46E5) else Color(0xFF4338CA)
val TileTasbihStart: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFFA855F7) else Color(0xFF7C3AED)
val TileTasbihEnd: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF6366F1) else Color(0xFF5855EB)
val TileBookmarkStart: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF4F46E5) else Color(0xFF4338CA)
val TileBookmarkEnd: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF312E81) else Color(0xFF1E1B4B)
val TileSettingsStart: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF64748B) else Color(0xFF475569)
val TileSettingsEnd: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF334155) else Color(0xFF1E293B)

val CatJadwal: Color @Composable get() = AmberAccent

val GlassBackground: Color @Composable get() = SurfaceCard
val GlassSurface: Color @Composable get() = SurfaceCard
val GlassNavbar: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF131B2E) else Color(0xFFFFFFFF)
val GlassBorder: Color @Composable get() = BorderSubtle
val GlassBorderGlow: Color @Composable get() = AmberAccent
val GlassHighlight: Color @Composable get() = Color.Transparent
val GlassCardPressed: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF1E2842) else Color(0xFFEEF1FB)
