package com.arbdevai.quranvip.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arbdevai.quranvip.data.model.TafsirAyah

@Composable
fun TafsirDialog(
    surahName: String,
    ayahNumber: Int,
    tafsir: TafsirAyah?,
    loading: Boolean,
    error: String?,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tafsir $surahName · Ayat $ayahNumber") },
        text = {
            when {
                loading -> CircularProgressIndicator()
                error != null -> Column {
                    Text(error)
                    TextButton(onClick = onRetry) { Text("Coba lagi") }
                }
                tafsir != null -> Text(
                    text = tafsir.teks,
                    modifier = Modifier
                        .heightIn(max = 420.dp)
                        .verticalScroll(rememberScrollState())
                )
                else -> Text("Tafsir tidak tersedia.")
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Tutup") } }
    )
}
