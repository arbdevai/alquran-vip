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
import com.arbdevai.quranvip.ui.components.GlassCard
import com.arbdevai.quranvip.ui.components.QuranAyahCard
import com.arbdevai.quranvip.ui.components.QuranHeroBanner
import com.arbdevai.quranvip.ui.theme.*

@Composable
fun QuranScreen(state: UiState, viewModel: AppViewModel, modifier: Modifier = Modifier) {
    if (state.reader != null || state.readerLoading || state.readerError != null) {
        ReaderScreen(state, viewModel, modifier)
    } else {
        SurahListScreen(state, viewModel, modifier)
    }
}

@Composable
private fun SurahListScreen(state: UiState, viewModel: AppViewModel, modifier: Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BgCanvas),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Hero Banner Al-Qur'an (Yasin emerald theme with watermark ۞)
        item {
            QuranHeroBanner(
                title = "Al-Qur'an Al-Karim",
                subtitle = "114 Surah · 30 Juz · 6236 Ayat",
                tag = "KITABULLAH",
                arabicWatermark = "۞",
                badges = listOf("114 Surah", "30 Juz", "Kemenag RI"),
                trailingContent = {
                    FilledIconButton(
                        onClick = { viewModel.setScreen(AppScreen.SETTINGS) },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Color.Black.copy(alpha = 0.35f),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(Icons.Default.Tune, contentDescription = "Pengaturan")
                    }
                }
            )
        }

        // 2. Search Field Bar
        item {
            OutlinedTextField(
                value = state.surahQuery,
                onValueChange = viewModel::setSurahQuery,
                placeholder = { Text("Cari nomor atau nama surah...", color = TextPlaceholder, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = AmberAccent) },
                trailingIcon = {
                    if (state.surahQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSurahQuery("") }) {
                            Icon(Icons.Default.Clear, "Bersihkan", tint = TextSecondary)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(22.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceInput,
                    unfocusedContainerColor = SurfaceInput,
                    focusedBorderColor = AmberAccent,
                    unfocusedBorderColor = BorderSubtle,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 3. Surah List Content
        when {
            state.quranLoading && state.surahs.isEmpty() -> {
                item { LoadingView("Memuat 114 Surah Al-Qur'an...") }
            }
            state.quranError != null && state.surahs.isEmpty() -> {
                item { ErrorView(state.quranError, viewModel::loadSurahs) }
            }
            else -> {
                items(state.filteredSurahs, key = { it.nomor }) { surah ->
                    SurahListItem(surah) { viewModel.openSurah(surah.nomor) }
                }
                if (state.filteredSurahs.isEmpty()) {
                    item { EmptyView("Surah \"${state.surahQuery}\" tidak ditemukan") }
                }
            }
        }
    }
}

@Composable
private fun SurahListItem(surah: Surah, onClick: () -> Unit) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(SurfacePill),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${surah.nomor}",
                    color = AmberAccent,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = surah.namaLatin,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${surah.arti} • ${surah.jumlahAyat} Ayat • ${surah.tempatTurun}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
            Text(
                text = surah.nama,
                fontFamily = ArabicFontFamily,
                style = MaterialTheme.typography.headlineMedium,
                color = AmberAccent,
                textAlign = TextAlign.End
            )
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
        if (reader != null && reader.ayat.isNotEmpty()) {
            listState.scrollToItem(startIndex.coerceAtMost(reader.ayat.lastIndex))
        }
    }

    Column(modifier = modifier.fillMaxSize().background(BgCanvas)) {
        when {
            state.readerLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LoadingView("Membuka surah...")
                }
            }
            state.readerError != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ErrorView(state.readerError, viewModel::retryReader)
                }
            }
            reader == null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    EmptyView("Surah belum tersedia")
                }
            }
            else -> {
                Box(Modifier.fillMaxSize()) {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // 1. Hero Card Surah Reader Header (Yasin style with ۞)
                        item {
                            val qoriName = Reciters.names[state.preferences.qori] ?: "Qari Pilihan"
                            QuranHeroBanner(
                                title = "Surah ${reader.namaLatin}",
                                subtitle = "${reader.arti} · ${reader.tempatTurun} · ${reader.jumlahAyat} Ayat",
                                tag = "SURAH KE-${reader.nomor}",
                                arabicWatermark = "۞",
                                badges = listOf(reader.tempatTurun, "${reader.jumlahAyat} Ayat", qoriName),
                                trailingContent = {
                                    FilledIconButton(
                                        onClick = { showReciters = true },
                                        colors = IconButtonDefaults.filledIconButtonColors(
                                            containerColor = Color.Black.copy(alpha = 0.35f),
                                            contentColor = Color.White
                                        ),
                                        modifier = Modifier.size(38.dp)
                                    ) {
                                        Icon(Icons.Default.RecordVoiceOver, contentDescription = "Pilih Qari")
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    FilledIconButton(
                                        onClick = viewModel::closeReader,
                                        colors = IconButtonDefaults.filledIconButtonColors(
                                            containerColor = Color.Black.copy(alpha = 0.35f),
                                            contentColor = Color.White
                                        ),
                                        modifier = Modifier.size(38.dp)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Tutup")
                                    }
                                }
                            )
                        }

                        // 2. Bismillah Decorative Card (kecuali Surah At-Taubah no 9 dan Al-Fatihah no 1)
                        if (reader.nomor != 1 && reader.nomor != 9) {
                            item {
                                GlassCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(18.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(18.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "بِسْمِ اللّٰهِ الرَّحْمٰنِ الرَّحِيْمِ",
                                            fontFamily = ArabicFontFamily,
                                            fontSize = 28.sp,
                                            lineHeight = 50.sp,
                                            lineHeightStyle = ArabicLineHeightStyle,
                                            color = AmberAccent,
                                            textAlign = TextAlign.Center,
                                            style = TextStyle(
                                                textDirection = TextDirection.Rtl,
                                                platformStyle = PlatformTextStyle(includeFontPadding = false)
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        // 3. Ayat Cards (Three-tier hierarchy from Sahalarbani/yasin)
                        items(reader.ayat, key = { it.nomorAyat }) { ayah ->
                            val playing = state.playback.playing &&
                                state.playback.surah == reader.nomor &&
                                state.playback.ayah == ayah.nomorAyat
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
                            LaunchedEffect(ayah.nomorAyat) {
                                snapshotFlow { listState.firstVisibleItemIndex }.collect { index ->
                                    if (index >= 1 && reader.ayat.getOrNull(index - 1)?.nomorAyat == ayah.nomorAyat) {
                                        viewModel.recordReading(ayah.nomorAyat)
                                    }
                                }
                            }
                        }
                    }

                    // Floating Bottom Toolbar (A- / A+ & Putar Surah)
                    FloatingQuranController(
                        isPlayingAll = state.playback.playing && state.playback.surah == reader.nomor && state.playback.ayah == 0,
                        onFontDecrease = { viewModel.setFontSize(state.preferences.fontSize - 1) },
                        onFontIncrease = { viewModel.setFontSize(state.preferences.fontSize + 1) },
                        onPlayAll = { if (state.playback.playing) viewModel.togglePlayback() else viewModel.playSurah() },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }

    if (showReciters) {
        ReciterDialog(state, viewModel) { showReciters = false }
    }
    if (state.tafsirAyah != null) {
        TafsirDialog(state, viewModel)
    }
}

@Composable
private fun ReciterDialog(state: UiState, viewModel: AppViewModel, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Pilih Qari Audio",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Reciters.names.forEach { (id, name) ->
                    val isSelected = state.preferences.qori == id
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        containerColor = if (isSelected) AmberAccent.copy(alpha = 0.15f) else SurfaceCard,
                        borderColor = if (isSelected) AmberAccent else BorderSubtle,
                        onClick = {
                            viewModel.setQori(id)
                            onDismiss()
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                name,
                                color = if (isSelected) AmberAccent else TextPrimary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                            if (isSelected) {
                                Icon(Icons.Default.Check, null, tint = AmberAccent)
                            }
                        }
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
    val ayahNum = state.tafsirAyah ?: return
    val text = state.tafsir?.tafsir?.firstOrNull { it.ayat == ayahNum }?.teks
    AlertDialog(
        onDismissRequest = viewModel::dismissTafsir,
        title = {
            Text(
                "Tafsir ${state.reader?.namaLatin ?: ""} · Ayat $ayahNum",
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            when {
                state.tafsirLoading -> CircularProgressIndicator(color = AmberAccent)
                state.tafsirError != null -> Text(state.tafsirError, color = TextSecondary)
                text != null -> Text(text, color = TextTranslation, lineHeight = 22.sp)
                else -> Text("Tafsir untuk ayat ini belum tersedia.", color = TextSecondary)
            }
        },
        confirmButton = { TextButton(onClick = viewModel::dismissTafsir) { Text("Tutup", color = AmberAccent) } },
        containerColor = SurfaceCard
    )
}

@Composable
internal fun LoadingView(message: String) {
    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AmberAccent)
            Spacer(Modifier.height(12.dp))
            Text(message, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
    }
}

@Composable
internal fun ErrorView(message: String?, retry: () -> Unit) {
    Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.CloudOff, null, tint = TextSecondary, modifier = Modifier.size(40.dp))
            Spacer(Modifier.height(10.dp))
            Text(message ?: "Terjadi kesalahan", style = MaterialTheme.typography.bodyMedium, color = TextSecondary, textAlign = TextAlign.Center)
            Spacer(Modifier.height(12.dp))
            Button(onClick = retry, colors = ButtonDefaults.buttonColors(containerColor = AmberAccent, contentColor = Color.Black)) {
                Text("Coba Lagi")
            }
        }
    }
}

@Composable
internal fun EmptyView(message: String) {
    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
        Text(message, style = MaterialTheme.typography.bodyMedium, color = TextSecondary, textAlign = TextAlign.Center)
    }
}
