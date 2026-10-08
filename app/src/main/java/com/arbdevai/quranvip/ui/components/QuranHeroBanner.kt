package com.arbdevai.quranvip.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbdevai.quranvip.ui.theme.*

@Composable
fun QuranHeroBanner(
    title: String,
    subtitle: String,
    tag: String? = null,
    arabicWatermark: String = "۞",
    arabicQuote: String? = null,
    badges: List<String> = emptyList(),
    gradientColors: List<Color> = listOf(EmeraldGradientStart, EmeraldGradientMid, EmeraldGradientEnd),
    trailingContent: @Composable (RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.linearGradient(
                    colors = gradientColors
                )
            )
            .border(
                BorderStroke(
                    1.dp,
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.35f),
                            Color.White.copy(alpha = 0.08f),
                            AmberAccent.copy(alpha = 0.40f)
                        )
                    )
                ),
                RoundedCornerShape(26.dp)
            )
            .padding(20.dp)
    ) {
        // Islamic Watermark Glyph (۞) in background bottom-right
        Text(
            text = arabicWatermark,
            fontFamily = ArabicFontFamily,
            fontSize = 115.sp,
            color = Color.White.copy(alpha = 0.12f),
            textAlign = TextAlign.End,
            style = TextStyle(
                textDirection = TextDirection.Rtl,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 18.dp, y = 28.dp)
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f, fill = false)) {
                    // Eyebrow Status Tag
                    if (tag != null) {
                        Surface(
                            color = Color.Black.copy(alpha = 0.32f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .border(
                                    BorderStroke(0.6.dp, Color.White.copy(alpha = 0.20f)),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(bottom = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "✦",
                                    color = AmberAccent,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Text(
                                    text = tag.uppercase(),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // Grand Display Title
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Subtitle
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.90f),
                        lineHeight = 20.sp
                    )
                }

                // Trailing Action Button
                if (trailingContent != null) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        trailingContent()
                    }
                }
            }

            // Arabic Calligraphy Quote (RTL, right-aligned)
            if (arabicQuote != null) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = arabicQuote,
                    color = Color.White.copy(alpha = 0.95f),
                    textAlign = TextAlign.End,
                    style = TextStyle(
                        fontFamily = ArabicFontFamily,
                        fontSize = 23.sp,
                        lineHeight = 42.sp,
                        lineHeightStyle = ArabicLineHeightStyle,
                        textDirection = TextDirection.Rtl,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Stat Badges
            if (badges.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    badges.forEach { badgeText ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.Black.copy(alpha = 0.25f),
                            modifier = Modifier.border(
                                BorderStroke(0.6.dp, Color.White.copy(alpha = 0.15f)),
                                RoundedCornerShape(10.dp)
                            )
                        ) {
                            Text(
                                text = badgeText,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.95f),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.5.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
