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

// Quranic Arabic Font: Scheherazade New (Ottoman Naskh Style with perfect harakat kerning)
val ArabicFontFamily = FontFamily(
    Font(R.font.scheherazade_regular, FontWeight.Normal),
    Font(R.font.scheherazade_bold, FontWeight.Bold)
)

// Clean International UI Font: Inter
val LatinFontFamily = FontFamily(
    Font(R.font.inter, FontWeight.Normal),
    Font(R.font.inter, FontWeight.Medium),
    Font(R.font.inter, FontWeight.SemiBold),
    Font(R.font.inter, FontWeight.Bold)
)

val ArabicLineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None
)

val QuranTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = ArabicFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 72.sp,
        textDirection = TextDirection.Rtl,
        lineHeightStyle = ArabicLineHeightStyle,
        color = TextPrimary
    ),
    displayMedium = TextStyle(
        fontFamily = ArabicFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
        lineHeight = 60.sp,
        textDirection = TextDirection.Rtl,
        lineHeightStyle = ArabicLineHeightStyle,
        color = TextPrimary
    ),
    displaySmall = TextStyle(
        fontFamily = ArabicFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 25.sp,
        lineHeight = 50.sp,
        textDirection = TextDirection.Rtl,
        lineHeightStyle = ArabicLineHeightStyle,
        color = TextPrimary
    ),
    headlineMedium = TextStyle(
        fontFamily = ArabicFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 42.sp,
        textDirection = TextDirection.Rtl,
        lineHeightStyle = ArabicLineHeightStyle,
        color = AmberAccent
    ),
    titleLarge = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.1.sp,
        color = TextPrimary
    ),
    titleMedium = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = TextPrimary
    ),
    titleSmall = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = TextPrimary
    ),
    bodyLarge = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 26.sp,
        color = TextTranslation
    ),
    bodyMedium = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        color = TextTransliteration
    ),
    bodySmall = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.5.sp,
        lineHeight = 22.sp,
        color = TextTranslation
    ),
    labelLarge = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = TextPrimary
    ),
    labelMedium = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 11.5.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.8.sp,
        color = TextSecondary
    ),
    labelSmall = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        color = TextSecondary
    )
)
