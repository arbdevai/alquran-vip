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
import androidx.compose.ui.text.style.TextAlign
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
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = if (isPlaying) SurfaceInput else SurfaceCard),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.cardPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(SurfacePill),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${ayah.nomorAyat}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
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
                        Icon(Icons.Default.MoreVert, contentDescription = "Tafsir", tint = TextSecondary)
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

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = ayah.teksArab,
                fontSize = fontSize.sp,
                lineHeight = (fontSize + 22).sp,
                fontFamily = ArabicFontFamily,
                color = TextPrimary,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            if (showLatin) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = ayah.teksLatin, style = MaterialTheme.typography.bodyMedium)
            }

            if (showTranslation) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = ayah.teksIndonesia, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
