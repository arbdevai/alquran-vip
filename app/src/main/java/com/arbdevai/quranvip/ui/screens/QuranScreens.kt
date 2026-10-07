package com.arbdevai.quranvip.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.arbdevai.quranvip.data.model.Reciters
import com.arbdevai.quranvip.data.model.Surah
import com.arbdevai.quranvip.ui.AppScreen
import com.arbdevai.quranvip.ui.AppViewModel
import com.arbdevai.quranvip.ui.UiState
import com.arbdevai.quranvip.ui.components.CalmIconTile
import com.arbdevai.quranvip.ui.components.CalmRowItem
import com.arbdevai.quranvip.ui.components.CalmScreenTitle
import com.arbdevai.quranvip.ui.components.QuietIconButton
import com.arbdevai.quranvip.ui.theme.AmberAccent
import com.arbdevai.quranvip.ui.theme.ArabicFontFamily
import com.arbdevai.quranvip.ui.theme.ArabicLineHeightStyle
import com.arbdevai.quranvip.ui.theme.BgCanvas
import com.arbdevai.quranvip.ui.theme.BorderSubtle
import com.arbdevai.quranvip.ui.theme.SurfaceCard
import com.arbdevai.quranvip.ui.theme.SurfaceInput
import com.arbdevai.quranvip.ui.theme.SurfacePill
import com.arbdevai.quranvip.ui.theme.TextPrimary
import com.arbdevai.quranvip.ui.theme.TextSecondary
import com.arbdevai.quranvip.ui.theme.TextTranslation
import com.arbdevai.quranvip.ui.theme.TextTransliteration
import kotlinx.coroutines.launch

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
        modifier = modifier.fillMaxSize().background(BgCanvas),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                CalmScreenTitle(
                    title = "Al-Qur'an",
                    subtitle = "Pilih surah untuk mulai membaca",
                    modifier = Modifier.weight(1f)
                )
                QuietIconButton(
                    icon = Icons.Default.Settings,
                    contentDescription = "Pengaturan",
                    onClick = { viewModel.setScreen(AppScreen.SETTINGS) }
                )
            }
        }

        item {
            OutlinedTextField(
                value = state.surahQuery,
                onValueChange = viewModel::setSurahQuery,
                singleLine = true,
                placeholder = { Text("Cari nama atau nomor surah") },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = TextSecondary) },
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceInput,
                    unfocusedContainerColor = SurfaceInput,
                    focusedBorderColor = AmberAccent,
                    unfocusedBorderColor = BorderSubtle,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedPlaceholderColor = TextSecondary,
                    unfocusedPlaceholderColor = TextSecondary
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        when {
            state.quranLoading && state.surahs.isEmpty() -> item { LoadingView("Memuat daftar surah...") }
            state.quranError != null && state.surahs.isEmpty() -> item { ErrorView(state.quranError, viewModel::loadSurahs) }
            else -> {
                items(state.filteredSurahs, key = { it.nomor }) { surah ->
                    SurahListItem(surah) { viewModel.openSurah(surah.nomor) }
                }
                if (state.filteredSurahs.isEmpty()) item { EmptyView("Surah tidak ditemukan") }
            }
        }
    }
}

@Composable
private fun SurahListItem(surah: Surah, onClick: () -> Unit) {
    CalmRowItem(
        leading = {
            CalmIconTile(
                icon = Icons.Default.MenuBook,
                tint = AmberAccent,
                contentDescription = null
            )
        },
        title = "${surah.nomor}. ${surah.namaLatin}",
        subtitle = "${surah.arti} · ${surah.jumlahAyat} ayat · ${surah.tempatTurun}",
        trailing = {
            Text(
                text = surah.nama,
                fontFamily = ArabicFontFamily,
                fontSize = 21.sp,
                color = TextPrimary,
                textAlign = TextAlign.End
            )
        },
        onClick = onClick
    )
}

@Composable
private fun ReaderScreen(state: UiState, viewModel: AppViewModel, modifier: Modifier) {
    when {
        state.readerLoading -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { LoadingView("Membuka surah...") }
        state.readerError != null -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { ErrorView(state.readerError, viewModel::retryReader) }
        state.reader == null -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { EmptyView("Surah belum tersedia") }
        else -> SacredReader(state.reader, state, viewModel, modifier)
    }
}

@Composable
private fun SacredReader(reader: Surah, state: UiState, viewModel: AppViewModel, modifier: Modifier) {
    var selectedAyahNumber by rememberSaveable(reader.nomor) { mutableIntStateOf(0) }
    var reciterSheet by rememberSaveable { mutableStateOf(false) }
    val listState = rememberLazyListState()

    LaunchedEffect(reader.nomor, state.readerStartAyah) {
        val index = (state.readerStartAyah - 1).coerceIn(0, reader.ayat.lastIndex.coerceAtLeast(0))
        listState.scrollToItem(index + 1)
    }

    Column(modifier = modifier.fillMaxSize().background(BgCanvas)) {
        ReaderHeader(
            reader = reader,
            qori = Reciters.names[state.preferences.qori] ?: "Qari",
            onBack = viewModel::closeReader,
            onPickQori = { reciterSheet = true }
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 116.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item {
                ReaderIntro(reader)
            }
            items(reader.ayat, key = { it.nomorAyat }) { ayah ->
                val selected = selectedAyahNumber == ayah.nomorAyat
                val playing = state.playback.playing && state.playback.surah == reader.nomor && state.playback.ayah == ayah.nomorAyat
                AyahReadingBlock(
                    ayah = ayah,
                    fontSize = state.preferences.fontSize,
                    showLatin = state.preferences.showLatin,
                    showTranslation = state.preferences.showTranslation,
                    selected = selected,
                    playing = playing,
                    bookmarked = state.preferences.bookmarks.any { it.surah == reader.nomor && it.ayah == ayah.nomorAyat },
                    onSelect = {
                        selectedAyahNumber = if (selected) 0 else ayah.nomorAyat
                        viewModel.recordReading(ayah.nomorAyat)
                    },
                    onPlay = { viewModel.playAyah(ayah.nomorAyat) },
                    onBookmark = { viewModel.toggleBookmark(ayah) },
                    onTafsir = { viewModel.loadTafsir(ayah.nomorAyat) }
                )
            }
        }

        ReaderPlaybackBar(
            isPlaying = state.playback.playing,
            onFontDecrease = { viewModel.setFontSize(state.preferences.fontSize - 1) },
            onFontIncrease = { viewModel.setFontSize(state.preferences.fontSize + 1) },
            onPlay = { if (state.playback.playing) viewModel.togglePlayback() else viewModel.playSurah() }
        )
    }

    if (reciterSheet) ReciterDialog(state, viewModel) { reciterSheet = false }
    if (state.tafsirAyah != null) TafsirDialog(state, viewModel)
}

@Composable
private fun ReaderHeader(reader: Surah, qori: String, onBack: () -> Unit, onPickQori: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        QuietIconButton(Icons.Default.ArrowBack, "Kembali", onBack)
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(reader.namaLatin, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
            Text(qori, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
        QuietIconButton(Icons.Default.RecordVoiceOver, "Pilih qari", onPickQori, tint = AmberAccent)
    }
}

@Composable
private fun ReaderIntro(reader: Surah) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(reader.nama, style = MaterialTheme.typography.displaySmall, color = TextPrimary, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Text("${reader.arti} · ${reader.tempatTurun} · ${reader.jumlahAyat} ayat", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
        if (reader.nomor != 1 && reader.nomor != 9) {
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                "بِسْمِ اللّٰهِ الرَّحْمٰنِ الرَّحِيْمِ",
                fontFamily = ArabicFontFamily,
                fontSize = 27.sp,
                lineHeight = 54.sp,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun AyahReadingBlock(
    ayah: Ayah,
    fontSize: Int,
    showLatin: Boolean,
    showTranslation: Boolean,
    selected: Boolean,
    playing: Boolean,
    bookmarked: Boolean,
    onSelect: () -> Unit,
    onPlay: () -> Unit,
    onBookmark: () -> Unit,
    onTafsir: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(vertical = 17.dp)
    ) {
        Text(
            text = ayah.teksArab,
            color = if (playing) AmberAccent else TextPrimary,
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

        if (showLatin) {
            Spacer(modifier = Modifier.height(13.dp))
            Text(
                ayah.teksLatin,
                style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                color = TextTransliteration
            )
        }

        if (showTranslation) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(ayah.teksIndonesia, style = MaterialTheme.typography.bodySmall, color = TextTranslation)
        }

        Spacer(modifier = Modifier.height(14.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = SurfacePill) {
                Text(
                    "${ayah.nomorAyat}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                )
            }
            if (selected) {
                Spacer(modifier = Modifier.width(12.dp))
                TextButton(onClick = onPlay) {
                    Icon(if (playing) Icons.Default.Pause else Icons.Default.PlayArrow, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (playing) "Jeda" else "Dengar")
                }
                TextButton(onClick = onBookmark) {
                    Icon(if (bookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder, null, modifier = Modifier.size(17.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (bookmarked) "Tersimpan" else "Simpan")
                }
                TextButton(onClick = onTafsir) { Text("Tafsir") }
            }
        }
    }
}

@Composable
private fun ReaderPlaybackBar(isPlaying: Boolean, onFontDecrease: () -> Unit, onFontIncrease: () -> Unit, onPlay: () -> Unit) {
    Surface(color = SurfaceCard, shadowElevation = 10.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 20.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TextButton(onClick = onFontDecrease) { Text("A−") }
            TextButton(onClick = onFontIncrease) { Text("A+") }
            Spacer(modifier = Modifier.weight(1f))
            FilledIconButton(
                onClick = onPlay,
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = AmberAccent, contentColor = BgCanvas)
            ) { Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, if (isPlaying) "Jeda" else "Putar surah") }
            Text(if (isPlaying) "Sedang diputar" else "Putar surah", style = MaterialTheme.typography.labelLarge, color = TextPrimary)
        }
    }
}

@Composable
private fun ReciterDialog(state: UiState, viewModel: AppViewModel, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pilih qari", color = TextPrimary) },
        text = {
            Column {
                Reciters.names.forEach { (id, name) ->
                    val selected = state.preferences.qori == id
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { viewModel.setQori(id); onDismiss() }.padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(name, modifier = Modifier.weight(1f), color = if (selected) AmberAccent else TextPrimary)
                        if (selected) Icon(Icons.Default.Bookmark, null, tint = AmberAccent)
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Tutup") } },
        containerColor = SurfaceCard
    )
}

@Composable
private fun TafsirDialog(state: UiState, viewModel: AppViewModel) {
    val ayahNumber = state.tafsirAyah ?: return
    val text = state.tafsir?.tafsir?.firstOrNull { it.ayat == ayahNumber }?.teks
    AlertDialog(
        onDismissRequest = viewModel::dismissTafsir,
        title = { Text("Tafsir ayat $ayahNumber", color = TextPrimary) },
        text = {
            when {
                state.tafsirLoading -> CircularProgressIndicator(color = AmberAccent)
                state.tafsirError != null -> Text(state.tafsirError, color = TextSecondary)
                text != null -> Text(text, style = MaterialTheme.typography.bodyLarge, color = TextTranslation)
                else -> Text("Tafsir belum tersedia.", color = TextSecondary)
            }
        },
        confirmButton = { TextButton(onClick = viewModel::dismissTafsir) { Text("Tutup") } },
        containerColor = SurfaceCard
    )
}

@Composable
internal fun LoadingView(message: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(32.dp)) {
        CircularProgressIndicator(color = AmberAccent)
        Spacer(modifier = Modifier.height(12.dp))
        Text(message, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
internal fun ErrorView(message: String?, retry: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(32.dp)) {
        Text(message ?: "Terjadi kesalahan", color = TextSecondary, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(10.dp))
        TextButton(onClick = retry) { Text("Coba lagi") }
    }
}

@Composable
internal fun EmptyView(message: String) {
    Text(message, color = TextSecondary, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(32.dp))
}
