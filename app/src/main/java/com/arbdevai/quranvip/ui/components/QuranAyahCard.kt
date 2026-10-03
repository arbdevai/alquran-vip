package com.arbdevai.quranvip.ui.components

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

            // 1. Teks Arab Al-Qur'an (RTL, LineHeightStyle centered so diacritics never clip)
            Text(
                text = ayah.teksArab,
                fontSize = fontSize.sp,
                lineHeight = (fontSize * 1.95f).sp,
                lineHeightStyle = ArabicLineHeightStyle,
                fontFamily = ArabicFontFamily,
                fontWeight = FontWeight.Normal,
                color = TextPrimary,
                textAlign = TextAlign.End,
                style = TextStyle(
                    textDirection = TextDirection.Rtl,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // 2. Transliterasi Latin Fonetik (Golden Sand, Italic, Relaxed Line Height)
            if (showLatin) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = ayah.teksLatin,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = FontStyle.Italic
                    ),
                    color = TextTransliteration,
                    lineHeight = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 3. Terjemahan Bahasa Indonesia (High-legibility neutral gray, 22sp line height)
            if (showTranslation) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = ayah.teksIndonesia,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTranslation,
                    lineHeight = 22.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
