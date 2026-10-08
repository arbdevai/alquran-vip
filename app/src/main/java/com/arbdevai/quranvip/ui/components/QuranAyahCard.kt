package com.arbdevai.quranvip.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbdevai.quranvip.data.model.Ayah
import com.arbdevai.quranvip.ui.theme.*

@Composable
fun AudioWaveVisualizer(
    color: Color = AmberAccent,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "audioWave")
    val h1 by transition.animateFloat(
        initialValue = 4f, targetValue = 18f,
        animationSpec = infiniteRepeatable(tween(450, easing = LinearEasing), RepeatMode.Reverse),
        label = "h1"
    )
    val h2 by transition.animateFloat(
        initialValue = 16f, targetValue = 6f,
        animationSpec = infiniteRepeatable(tween(550, easing = LinearEasing), RepeatMode.Reverse),
        label = "h2"
    )
    val h3 by transition.animateFloat(
        initialValue = 6f, targetValue = 20f,
        animationSpec = infiniteRepeatable(tween(400, easing = LinearEasing), RepeatMode.Reverse),
        label = "h3"
    )
    val h4 by transition.animateFloat(
        initialValue = 14f, targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(500, easing = LinearEasing), RepeatMode.Reverse),
        label = "h4"
    )

    Row(
        modifier = modifier.height(20.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.width(3.dp).height(h1.dp).clip(CircleShape).background(color))
        Box(Modifier.width(3.dp).height(h2.dp).clip(CircleShape).background(color))
        Box(Modifier.width(3.dp).height(h3.dp).clip(CircleShape).background(color))
        Box(Modifier.width(3.dp).height(h4.dp).clip(CircleShape).background(color))
    }
}

@Composable
fun QuranAyahCard(
    ayah: Ayah,
    fontSize: Int,
    showLatin: Boolean,
    showTranslation: Boolean,
    isBookmarked: Boolean,
    isPlaying: Boolean,
    onBookmark: () -> Unit,
    onPlayAudio: () -> Unit,
    onTafsir: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        containerColor = if (isPlaying) GlassCardPressed else GlassSurface,
        borderColor = if (isPlaying) AmberAccent else GlassBorder,
        borderWidth = if (isPlaying) 1.5.dp else 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.cardPadding)
        ) {
            // Verse Header Row (Number badge + Action Buttons)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isPlaying) AmberAccent else SurfacePill),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${ayah.nomorAyat}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isPlaying) Color.Black else Color.White
                        )
                    }

                    if (isPlaying) {
                        AudioWaveVisualizer(color = AmberAccent)
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBookmark, modifier = Modifier.size(38.dp)) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = if (isBookmarked) "Hapus bookmark" else "Simpan bookmark",
                            tint = if (isBookmarked) AmberAccent else Color.White
                        )
                    }
                    IconButton(onClick = onTafsir, modifier = Modifier.size(38.dp)) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Tafsir Kemenag", tint = TextSecondary)
                    }
                    FilledIconButton(
                        onClick = onPlayAudio,
                        modifier = Modifier.size(36.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = AmberAccent,
                            contentColor = Color.Black
                        )
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Jeda ayat" else "Putar ayat"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 1. Teks Arab Al-Qur'an (RTL - Mulai dari Sebelah Kanan)
            Text(
                text = ayah.teksArab,
                color = TextPrimary,
                textAlign = TextAlign.End,
                style = TextStyle(
                    fontFamily = ArabicFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = fontSize.sp,
                    lineHeight = (fontSize * 1.95f).sp,
                    lineHeightStyle = ArabicLineHeightStyle,
                    textDirection = TextDirection.Rtl,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // 2. Transliterasi Latin Fonetik (LTR - Mulai dari Sebelah Kiri, Emas Hangat)
            if (showLatin) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = ayah.teksLatin,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = FontStyle.Italic,
                        textDirection = TextDirection.Ltr,
                        lineHeight = 22.sp
                    ),
                    color = TextTransliteration,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 3. Terjemahan Bahasa Indonesia (LTR - Mulai dari Sebelah Kiri, Abu-abu Terang Bersih)
            if (showTranslation) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = ayah.teksIndonesia,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodySmall.copy(
                        textDirection = TextDirection.Ltr,
                        lineHeight = 24.sp
                    ),
                    color = TextTranslation,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
