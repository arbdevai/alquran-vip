package com.arbdevai.quranvip.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.arbdevai.quranvip.R

val ArabicFontFamily = FontFamily(Font(R.font.amiri_regular, FontWeight.Normal))
val LatinFontFamily = FontFamily(Font(R.font.jakarta_sans, FontWeight.Normal))

val QuranTypography = Typography(
    displayMedium = TextStyle(fontFamily = ArabicFontFamily, fontWeight = FontWeight.Normal, fontSize = 28.sp, lineHeight = 52.sp, color = TextPrimary),
    headlineMedium = TextStyle(fontFamily = ArabicFontFamily, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = AmberAccent),
    titleLarge = TextStyle(fontFamily = LatinFontFamily, fontWeight = FontWeight.Bold, fontSize = 20.sp, lineHeight = 26.sp, color = TextPrimary),
    titleMedium = TextStyle(fontFamily = LatinFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextPrimary),
    bodyMedium = TextStyle(fontFamily = LatinFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, lineHeight = 18.sp, color = TextTransliteration),
    bodySmall = TextStyle(fontFamily = LatinFontFamily, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 19.sp, color = TextSecondary),
    labelSmall = TextStyle(fontFamily = LatinFontFamily, fontWeight = FontWeight.Medium, fontSize = 11.sp, color = TextSecondary)
)
