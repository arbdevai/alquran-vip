package com.arbdevai.quranvip.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import com.arbdevai.quranvip.R

val ArabicFontFamily = FontFamily(
    Font(R.font.amiri_regular, FontWeight.Normal),
    Font(R.font.amiri_bold, FontWeight.Bold)
)

val LatinFontFamily = FontFamily(
    Font(R.font.jakarta_sans, FontWeight.Normal),
    Font(R.font.jakarta_sans, FontWeight.Medium),
    Font(R.font.jakarta_sans, FontWeight.SemiBold),
    Font(R.font.jakarta_sans, FontWeight.Bold)
)

val ArabicLineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None
)

val QuranTypography = Typography(
    // 1. Teks Arab Utama Ayat Al-Qur'an (Scalable)
    displayMedium = TextStyle(
        fontFamily = ArabicFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 56.sp,
        textDirection = TextDirection.Rtl,
        lineHeightStyle = ArabicLineHeightStyle,
        color = TextPrimary
    ),
    // 2. Tulisan Kaligrafi Nama Surah Arab
    headlineMedium = TextStyle(
        fontFamily = ArabicFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 36.sp,
        textDirection = TextDirection.Rtl,
        color = AmberAccent
    ),
    // 3. Judul Hero Card
    titleLarge = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.15.sp,
        color = TextPrimary
    ),
    // 4. Nama Surah Latin & Subheader
    titleMedium = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        color = TextPrimary
    ),
    // 5. Transliterasi Latin Fonetik
    bodyMedium = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.5.sp,
        lineHeight = 20.sp,
        color = TextTransliteration
    ),
    // 6. Terjemahan Bahasa Indonesia
    bodySmall = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        color = TextTranslation
    ),
    // 7. Eyebrow Tag / Kicker
    labelMedium = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 10.5.sp,
        lineHeight = 14.sp,
        letterSpacing = 1.2.sp,
        color = androidx.compose.ui.graphics.Color.White
    ),
    // 8. Label Sub-info & Metadata
    labelSmall = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.5.sp,
        lineHeight = 16.sp,
        color = TextSecondary
    )
)
