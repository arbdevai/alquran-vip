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

// Solid Hero Banner Surface
val HeroBannerBg: Color @Composable get() = AmberAccent

// Calendar Special Day Colors (Subtle & Themed)
val CalendarFridayBg: Color @Composable get() = AmberSoft
val CalendarFridayText: Color @Composable get() = AmberAccent
val CalendarSundayBg: Color @Composable get() = if (LocalThemeIsDark.current) Color(0x26EF5350) else Color(0x18DC2626)
val CalendarSundayText: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFFEF5350) else Color(0xFFDC2626)

// Speedometer Gauge Track
val GaugeTrackColor: Color @Composable get() = if (LocalThemeIsDark.current) Color(0x24FFFFFF) else Color(0xFFE2E6F5)

// ==========================================
// 3. Typography
// ==========================================
val TextPrimary: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFFF8FAFC) else Color(0xFF0F172A)
val TextTransliteration: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFFCBD5E1) else Color(0xFF475569)
val TextTranslation: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF94A3B8) else Color(0xFF64748B)
val TextSecondary: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF74829C) else Color(0xFF8A94A6)
val TextPlaceholder: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF475569) else Color(0xFFCBD5E1)

// ==========================================
// 4. Feature Tiles & Components (Solid Tonal)
// ==========================================
val CatJadwal: Color @Composable get() = AmberAccent

val GlassBackground: Color @Composable get() = SurfaceCard
val GlassSurface: Color @Composable get() = SurfaceCard
val GlassNavbar: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF131B2E) else Color(0xFFFFFFFF)
val GlassBorder: Color @Composable get() = BorderSubtle
val GlassBorderGlow: Color @Composable get() = AmberAccent
val GlassHighlight: Color @Composable get() = Color.Transparent
val GlassCardPressed: Color @Composable get() = if (LocalThemeIsDark.current) Color(0xFF1E2842) else Color(0xFFEEF1FB)
