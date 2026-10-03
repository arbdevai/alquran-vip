package com.arbdevai.quranvip.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arbdevai.quranvip.ui.theme.AmberAccent
import com.arbdevai.quranvip.ui.theme.SurfaceInput

@Composable
fun FloatingQuranController(
    isPlayingAll: Boolean,
    onFontDecrease: () -> Unit,
    onFontIncrease: () -> Unit,
    onPlayAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        color = SurfaceInput,
        tonalElevation = 6.dp,
        modifier = modifier.padding(bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(onClick = onFontDecrease) {
                Text("A-", color = AmberAccent, fontWeight = FontWeight.Bold)
            }
            TextButton(onClick = onFontIncrease) {
                Text("A+", color = AmberAccent, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onPlayAll,
                colors = ButtonDefaults.buttonColors(containerColor = AmberAccent),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = if (isPlayingAll) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    if (isPlayingAll) "Jeda Surah" else "Putar Semua",
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
