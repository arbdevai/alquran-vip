package com.arbdevai.quranvip.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbdevai.quranvip.data.model.Ayah
import com.arbdevai.quranvip.data.model.Bookmark
import com.arbdevai.quranvip.data.model.Reciters
import com.arbdevai.quranvip.data.model.Surah
import com.arbdevai.quranvip.ui.AppScreen
import com.arbdevai.quranvip.ui.AppViewModel
import com.arbdevai.quranvip.ui.UiState
import com.arbdevai.quranvip.ui.components.FloatingQuranController
import com.arbdevai.quranvip.ui.components.QuranAyahCard
import com.arbdevai.quranvip.ui.theme.*

@Composable
fun QuranScreen(state: UiState, viewModel: AppViewModel, modifier: Modifier = Modifier) {
    val reader = state.reader
    if (reader != null || state.readerLoading || state.readerError != null) {
        ReaderScreen(state, viewModel, modifier)
    } else {
        SurahListScreen(state, viewModel, modifier)
    }
}

@Composable
private fun SurahListScreen(state: UiState, viewModel: AppViewModel, modifier: Modifier) {
    Column(modifier = modifier.fillMaxSize().background(BgCanvas)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.screenPadding, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Al-Qur'an", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                Text("Baca dengan tenang, kapan saja", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
            IconButton(onClick = { viewModel.setScreen(AppScreen.SETTINGS) }) {
                Icon(Icons.Default.Tune, contentDescription = "Pengaturan", tint = TextSecondary)
            }
        }
        OutlinedTextField(
            value = state.surahQuery,
            onValueChange = viewModel::setSurahQuery,
            placeholder = { Text("Cari surah...", color = TextPlaceholder) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = TextSecondary) },
            trailingIcon = {
                if (state.surahQuery.isNotEmpty()) IconButton(onClick = { viewModel.setSurahQuery("") }) {
                    Icon(Icons.Default.Clear, "Bersihkan", tint = TextSecondary)
                }
            },
            singleLine = true,
            shape = MaterialTheme.shapes.extraLarge,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceInput,
                unfocusedContainerColor = SurfaceInput,
                focusedBorderColor = AmberAccent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.screenPadding)
        )
        Spacer(Modifier.height(12.dp))
        when {
            state.quranLoading && state.surahs.isEmpty() -> LoadingView("Memuat daftar surah...")
            state.quranError != null && state.surahs.isEmpty() -> ErrorView(state.quranError, viewModel::loadSurahs)
            else -> LazyColumn(
                contentPadding = PaddingValues(horizontal = Spacing.screenPadding, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.filteredSurahs, key = { it.nomor }) { surah -> SurahListItem(surah) { viewModel.openSurah(surah.nomor) } }
                if (state.filteredSurahs.isEmpty()) item { EmptyView("Surah tidak ditemukan") }
            }
        }
    }
}

@Composable
private fun SurahListItem(surah: Surah, onClick: () -> Unit) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(38.dp).clip(CircleShape).background(SurfacePill),
                contentAlignment = Alignment.Center
            ) { Text("${surah.nomor}", color = AmberAccent, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(surah.namaLatin, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                Text("${surah.arti} • ${surah.jumlahAyat} ayat • ${surah.tempatTurun}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            }
            Text(surah.nama, style = MaterialTheme.typography.headlineMedium, color = AmberAccent, textAlign = TextAlign.End)
        }
    }
}

@Composable
private fun ReaderScreen(state: UiState, viewModel: AppViewModel, modifier: Modifier) {
    val reader = state.reader
    var showReciters by rememberSaveable { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val startIndex = (state.readerStartAyah - 1).coerceAtLeast(0)
    LaunchedEffect(reader?.nomor, state.readerStartAyah) {
        if (reader != null && reader.ayat.isNotEmpty()) listState.scrollToItem(startIndex.coerceAtMost(reader.ayat.lastIndex))
    }
    Column(modifier = modifier.fillMaxSize().background(BgCanvas)) {
        ReaderTopBar(
            state = state,
            onBack = viewModel::closeReader,
            onReciter = { showReciters = true },
            onPlayAll = { if (state.playback.playing) viewModel.togglePlayback() else viewModel.playSurah() }
        )
        when {
            state.readerLoading -> LoadingView("Memuat surah...")
            state.readerError != null -> ErrorView(state.readerError, viewModel::retryReader)
            reader == null -> EmptyView("Surah belum tersedia")
            else -> Box(Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 92.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        ReaderHeader(reader)
                    }
                    items(reader.ayat, key = { it.nomorAyat }) { ayah ->
                        val playing = state.playback.playing && state.playback.surah == reader.nomor && state.playback.ayah == ayah.nomorAyat
                        QuranAyahCard(
                            ayah = ayah,
                            fontSize = state.preferences.fontSize,
                            showLatin = state.preferences.showLatin,
                            showTranslation = state.preferences.showTranslation,
                            isBookmarked = state.preferences.bookmarks.any { it.surah == reader.nomor && it.ayah == ayah.nomorAyat },
                            isPlaying = playing,
                            onBookmark = { viewModel.toggleBookmark(ayah) },
                            onPlayAudio = { viewModel.playAyah(ayah.nomorAyat) },
                            onTafsir = { viewModel.loadTafsir(ayah.nomorAyat) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        LaunchedEffect(ayah.nomorAyat) { snapshotFlow { listState.firstVisibleItemIndex }.collect { index ->
                            if (index >= 1 && reader.ayat.getOrNull(index - 1)?.nomorAyat == ayah.nomorAyat) viewModel.recordReading(ayah.nomorAyat)
                        } }
                    }
                }
                FloatingQuranController(
                    isPlayingAll = state.playback.playing,
                    onFontDecrease = { viewModel.setFontSize(state.preferences.fontSize - 1) },
                    onFontIncrease = { viewModel.setFontSize(state.preferences.fontSize + 1) },
                    onPlayAll = { if (state.playback.playing) viewModel.togglePlayback() else viewModel.playSurah() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
    if (showReciters) ReciterDialog(state, viewModel) { showReciters = false }
    if (state.tafsirAyah != null) TafsirDialog(state, viewModel)
}

@Composable
private fun ReaderTopBar(state: UiState, onBack: () -> Unit, onReciter: () -> Unit, onPlayAll: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Kembali", tint = TextPrimary) }
        Column(modifier = Modifier.weight(1f)) {
            Text(state.reader?.namaLatin ?: "Al-Qur'an", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
            Text(Reciters.names[state.preferences.qori] ?: "Qari Pilihan", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
        IconButton(onClick = onReciter) { Icon(Icons.Default.RecordVoiceOver, "Pilih qari", tint = AmberAccent) }
        IconButton(onClick = onPlayAll) {
            Icon(if (state.playback.playing) Icons.Default.Pause else Icons.Default.PlayArrow, "Putar", tint = AmberAccent)
        }
    }
}

@Composable
private fun ReaderHeader(reader: Surah) {
    Card(shape = MaterialTheme.shapes.large, colors = CardDefaults.cardColors(containerColor = SurfaceCard), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(reader.nama, style = MaterialTheme.typography.displaySmall, color = AmberAccent)
            Spacer(Modifier.height(4.dp))
            Text(reader.namaLatin, style = MaterialTheme.typography.titleLarge, color = TextPrimary)
            Text("${reader.arti} • ${reader.tempatTurun} • ${reader.jumlahAyat} ayat", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            if (reader.deskripsi.isNotBlank()) {
                Spacer(Modifier.height(12.dp))
                Text(reader.deskripsi, style = MaterialTheme.typography.bodySmall, color = TextSecondary, textAlign = TextAlign.Center, maxLines = 4)
            }
        }
    }
}

@Composable
private fun ReciterDialog(state: UiState, viewModel: AppViewModel, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pilih Qari", color = TextPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Reciters.names.forEach { (id, name) ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).clickable { viewModel.setQori(id); onDismiss() }.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = state.preferences.qori == id, onClick = null)
                        Spacer(Modifier.width(8.dp))
                        Text(name, color = TextPrimary)
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Tutup", color = AmberAccent) } },
        containerColor = SurfaceCard
    )
}

@Composable
private fun TafsirDialog(state: UiState, viewModel: AppViewModel) {
    val text = state.tafsir?.tafsir?.firstOrNull { it.ayat == state.tafsirAyah }?.teks
    AlertDialog(
        onDismissRequest = viewModel::dismissTafsir,
        title = { Text("Tafsir Ayat ${state.tafsirAyah}", color = TextPrimary) },
        text = {
            when {
                state.tafsirLoading -> CircularProgressIndicator(color = AmberAccent)
                state.tafsirError != null -> Text(state.tafsirError, color = TextSecondary)
                text != null -> Text(text, color = TextSecondary, lineHeight = 20.sp)
                else -> Text("Tafsir untuk ayat ini belum tersedia.", color = TextSecondary)
            }
        },
        confirmButton = { TextButton(onClick = viewModel::dismissTafsir) { Text("Tutup", color = AmberAccent) } },
        containerColor = SurfaceCard
    )
}

@Composable
internal fun LoadingView(message: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AmberAccent)
            Spacer(Modifier.height(12.dp))
            Text(message, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
    }
}

@Composable
internal fun ErrorView(message: String?, retry: () -> Unit) {
    Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.CloudOff, null, tint = TextSecondary, modifier = Modifier.size(40.dp))
            Spacer(Modifier.height(10.dp))
            Text(message ?: "Terjadi kesalahan", style = MaterialTheme.typography.bodyMedium, color = TextSecondary, textAlign = TextAlign.Center)
            Spacer(Modifier.height(10.dp))
            Button(onClick = retry, colors = ButtonDefaults.buttonColors(containerColor = AmberAccent, contentColor = Color.Black)) { Text("Coba Lagi") }
        }
    }
}

@Composable
internal fun EmptyView(message: String) {
    Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Text(message, style = MaterialTheme.typography.bodyMedium, color = TextSecondary, textAlign = TextAlign.Center)
    }
}
