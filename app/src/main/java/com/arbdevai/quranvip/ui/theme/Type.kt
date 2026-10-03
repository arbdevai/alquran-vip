package com.arbdevai.quranvip.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val ArabicFontFamily = FontFamily.Serif
val LatinFontFamily = FontFamily.SansSerif

val QuranTypography = Typography(
    // 1. Teks Arab Ayat Al-Qur'an
    displayMedium = TextStyle(
        fontFamily = ArabicFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 52.sp,
        color = TextPrimary
    ),
    // 2. Tulisan Nama Surah Kaligrafi Arab di List
    headlineMedium = TextStyle(
        fontFamily = ArabicFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        color = AmberAccent
    ),
    // 3. Judul Halaman & Hero Banner
    titleLarge = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        color = TextPrimary
    ),
    // 4. Nama Surah (Latin) & Judul Menu
    titleMedium = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        color = TextPrimary
    ),
    // 5. Transliterasi Latin
    bodyMedium = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        color = TextTransliteration
    ),
    // 6. Terjemahan Indonesia
    bodySmall = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 19.sp,
        color = TextSecondary
    ),
    // 7. Label Sub-info, Metadata, Badge
    labelSmall = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        color = TextSecondary
    )
)
