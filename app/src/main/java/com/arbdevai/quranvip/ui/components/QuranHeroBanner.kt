package com.arbdevai.quranvip.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
            .background(Brush.linearGradient(colors = gradientColors))
            .border(
                BorderStroke(
                    1.2.dp,
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.40f),
                            Color.White.copy(alpha = 0.08f),
                            AmberAccent.copy(alpha = 0.45f)
                        )
                    )
                ),
                RoundedCornerShape(26.dp)
            )
            .padding(20.dp)
    ) {
        // Islamic Watermark Emblem (۞) in background
        Text(
            text = arabicWatermark,
            fontFamily = ArabicFontFamily,
            fontSize = 120.sp,
            color = Color.White.copy(alpha = 0.12f),
            textAlign = TextAlign.End,
            style = TextStyle(
                textDirection = TextDirection.Rtl,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 16.dp, y = 26.dp)
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: Title/Tag + Trailing Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f, fill = false)) {
                    // Eyebrow Status Tag
                    if (tag != null) {
                        Surface(
                            color = Color.Black.copy(alpha = 0.35f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .border(
                                    BorderStroke(0.8.dp, AmberAccent.copy(alpha = 0.35f)),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(bottom = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "۞",
                                    fontFamily = ArabicFontFamily,
                                    color = AmberAccent,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(end = 6.dp)
                                )
                                Text(
                                    text = tag.uppercase(),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.2.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // Grand Display Title with generous line height
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 32.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Subtitle with clean breathing room
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.90f),
                        lineHeight = 22.sp
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

            // Arabic Calligraphy Quote with Islamic divider
            if (arabicQuote != null) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.15f),
                    thickness = 0.8.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = arabicQuote,
                    color = AmberAccent,
                    textAlign = TextAlign.End,
                    style = TextStyle(
                        fontFamily = ArabicFontFamily,
                        fontSize = 25.sp,
                        lineHeight = 44.sp,
                        lineHeightStyle = ArabicLineHeightStyle,
                        textDirection = TextDirection.Rtl,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Stat Badges Row
            if (badges.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    badges.forEach { badgeText ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.Black.copy(alpha = 0.28f),
                            modifier = Modifier.border(
                                BorderStroke(0.6.dp, Color.White.copy(alpha = 0.18f)),
                                RoundedCornerShape(10.dp)
                            )
                        ) {
                            Text(
                                text = badgeText,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.5.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
