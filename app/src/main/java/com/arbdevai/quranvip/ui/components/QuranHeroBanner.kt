package com.arbdevai.quranvip.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbdevai.quranvip.ui.theme.AmberAccent
import com.arbdevai.quranvip.ui.theme.EmeraldGradientEnd
import com.arbdevai.quranvip.ui.theme.EmeraldGradientStart
import com.arbdevai.quranvip.ui.theme.Spacing

@Composable
fun QuranHeroBanner(
    title: String,
    subtitle: String,
    tag: String? = null,
    badges: List<String> = emptyList(),
    gradientColors: List<Color> = listOf(EmeraldGradientStart, EmeraldGradientEnd),
    trailingContent: @Composable (RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
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
                            Color.White.copy(alpha = 0.38f),
                            Color.White.copy(alpha = 0.08f),
                            AmberAccent.copy(alpha = 0.35f)
                        )
                    )
                ),
                MaterialTheme.shapes.large
            )
            .padding(Spacing.cardPadding)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f, fill = false)) {
                    // 1. Eyebrow Tag / Kicker
                    if (tag != null) {
                        Surface(
                            color = Color.Black.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .border(
                                    BorderStroke(0.5.dp, Color.White.copy(alpha = 0.15f)),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(bottom = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "✦",
                                    color = AmberAccent,
                                    fontSize = 9.sp,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Text(
                                    text = tag.uppercase(),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White.copy(alpha = 0.95f)
                                )
                            }
                        }
                    }

                    // 2. Grand Display Title
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 3. Informative Subtitle
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.88f),
                        lineHeight = 18.sp
                    )
                }

                // 4. Interactive Trailing Action
                if (trailingContent != null) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        trailingContent()
                    }
                }
            }

            // 5. Stat Pills / Badges (Optional bottom chips)
            if (badges.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    badges.forEach { badgeText ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.Black.copy(alpha = 0.22f),
                            modifier = Modifier.border(
                                BorderStroke(0.5.dp, Color.White.copy(alpha = 0.12f)),
                                RoundedCornerShape(10.dp)
                            )
                        ) {
                            Text(
                                text = badgeText,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.95f),
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
