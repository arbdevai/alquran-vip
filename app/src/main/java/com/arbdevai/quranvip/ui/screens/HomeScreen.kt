package com.arbdevai.quranvip.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbdevai.quranvip.data.model.ReadingPosition
import com.arbdevai.quranvip.ui.AppScreen
import com.arbdevai.quranvip.ui.AppViewModel
import com.arbdevai.quranvip.ui.UiState
import com.arbdevai.quranvip.ui.components.AudioWaveVisualizer
import com.arbdevai.quranvip.ui.components.GlassCard
import com.arbdevai.quranvip.ui.components.QuranHeroBanner
import com.arbdevai.quranvip.ui.theme.*

@Composable
fun HomeScreen(state: UiState, viewModel: AppViewModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BgCanvas),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 0. Update Banner
        state.latestUpdate?.let { update ->
            item {
                UpdateBanner(update = update)
            }
        }

        // 1. Signature Emerald Hero Banner (Yasin Inspiration)
        item {
            val hijriText = state.selectedCalendar?.hijr?.let {
                "${it.day} ${it.monthName} ${it.year} H"
            } ?: "Kalender Hijriah"
            val cityText = state.preferences.city?.lokasi ?: "Lokasi GPS"

            QuranHeroBanner(
                title = "Al-Qur'an VIP",
                subtitle = "$hijriText · $cityText",
                arabicQuote = "اَلَا بِذِكْرِ اللّٰهِ تَطْمَىِٕنُّ الْقُلُوْبُ",
                badges = listOf(cityText, hijriText),
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

        // 2. Now Playing Mini Bar (if audio is active)
        if (state.playback.playing) {
            item {
                NowPlayingCard(
                    title = state.playback.title.ifBlank { "Sedang Memutar Audio" },
                    subtitle = state.playback.subtitle,
                    onToggle = viewModel::togglePlayback,
                    onStop = viewModel::stopPlayback,
                    onClick = {
                        if (state.playback.surah > 0) {
                            viewModel.openSurah(state.playback.surah, state.playback.ayah.coerceAtLeast(1))
                        }
                    }
                )
            }
        }

        // 3. Quick Surah Shortcuts (Yasin, Al-Mulk, Al-Kahfi, Al-Waqi'ah, Ayat Kursi)
        item {
            Column {
                Text(
                    text = "Surah Pilihan",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickSurahPill("Yasin", "36") { viewModel.openSurah(36, 1) }
                    QuickSurahPill("Al-Mulk", "67") { viewModel.openSurah(67, 1) }
                    QuickSurahPill("Al-Kahfi", "18") { viewModel.openSurah(18, 1) }
                    QuickSurahPill("Al-Waqi'ah", "56") { viewModel.openSurah(56, 1) }
                    QuickSurahPill("Ar-Rahman", "55") { viewModel.openSurah(55, 1) }
                    QuickSurahPill("Ayat Kursi", "2:255") { viewModel.openSurah(2, 255) }
                }
            }
        }

        // 4. Terakhir Dibaca Widget (Last Read)
        state.preferences.lastRead?.let { lastRead ->
            item {
                LastReadCard(
                    lastRead = lastRead,
                    onClick = { viewModel.openSurah(lastRead.surah, lastRead.ayah) }
                )
            }
        }

        // 5. 2-Column Menu Grid (Clean & Modern)
        item {
            Text(
                text = "Menu Utama",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Row 1: Al-Qur'an & Jadwal Salat
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FeatureTile(
                        title = "Al-Qur'an",
                        subtitle = "114 Surah & Audio",
                        icon = Icons.Default.MenuBook,
                        gradient = listOf(TileQuranStart, TileQuranEnd),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.setScreen(AppScreen.QURAN) }
                    )
                    FeatureTile(
                        title = "Jadwal Salat",
                        subtitle = "Waktu Adzan Harian",
                        icon = Icons.Default.AccessTime,
                        gradient = listOf(TileJadwalStart, TileJadwalEnd),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.setScreen(AppScreen.PRAYER) }
                    )
                }

                // Row 2: Kalender Hijriyah & Tasbih Digital
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FeatureTile(
                        title = "Kalender",
                        subtitle = "Penanggalan Hijriyah",
                        icon = Icons.Default.CalendarMonth,
                        gradient = listOf(TileQuranStart, TileJadwalEnd),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.setScreen(AppScreen.CALENDAR) }
                    )
                    FeatureTile(
                        title = "Tasbih Digital",
                        subtitle = "Penghitung Zikir",
                        icon = Icons.Default.TouchApp,
                        gradient = listOf(TileTasbihStart, TileTasbihEnd),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.setScreen(AppScreen.TASBIH) }
                    )
                }

                // Row 3: Ayat Disimpan & Pengaturan
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FeatureTile(
                        title = "Ayat Disimpan",
                        subtitle = "${state.preferences.bookmarks.size} Ayat Tersimpan",
                        icon = Icons.Default.Bookmark,
                        gradient = listOf(TileBookmarkStart, TileBookmarkEnd),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.setScreen(AppScreen.BOOKMARKS) }
                    )
                    FeatureTile(
                        title = "Pengaturan",
                        subtitle = "Qari & Huruf Arab",
                        icon = Icons.Default.Tune,
                        gradient = listOf(TileSettingsStart, TileSettingsEnd),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.setScreen(AppScreen.SETTINGS) }
                    )
                }
            }
        }

        // 6. Jadwal Shalat Hari Ini
        item {
            TodayPrayerCard(
                state = state,
                onOpenPrayer = { viewModel.setScreen(AppScreen.PRAYER) }
            )
        }
    }
}

@Composable
private fun QuickSurahPill(name: String, number: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCard,
        modifier = Modifier
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(AmberAccent.copy(alpha = 0.20f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberAccent
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun NowPlayingCard(
    title: String,
    subtitle: String,
    onToggle: () -> Unit,
    onStop: () -> Unit,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        borderColor = AmberAccent.copy(alpha = 0.60f),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AudioWaveVisualizer(color = AmberAccent)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (subtitle.isNotBlank()) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = AmberAccent,
                            maxLines = 1
                        )
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onToggle) {
                    Icon(Icons.Default.Pause, contentDescription = "Jeda", tint = AmberAccent)
                }
                IconButton(onClick = onStop) {
                    Icon(Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                }
            }
        }
    }
}

@Composable
private fun LastReadCard(
    lastRead: ReadingPosition,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(AmberAccent.copy(alpha = 0.16f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = null,
                        tint = AmberAccent,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Terakhir Dibaca",
                        style = MaterialTheme.typography.labelSmall,
                        color = AmberAccent,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Surah ${lastRead.name}",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Ayat ke-${lastRead.ayah}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
            FilledIconButton(
                onClick = onClick,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = AmberAccent,
                    contentColor = Color.Black
                ),
                modifier = Modifier.size(38.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Lanjutkan Membaca"
                )
            }
        }
    }
}

@Composable
private fun FeatureTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    gradient: List<Color>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.linearGradient(gradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun TodayPrayerCard(
    state: UiState,
    onOpenPrayer: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        onClick = onOpenPrayer
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(CatJadwal.copy(alpha = 0.20f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = CatJadwal,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Waktu Salat Hari Ini",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        val cityLokasi = state.preferences.city?.lokasi ?: "Lokasi GPS"
                        Text(
                            text = cityLokasi,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (state.preferences.city != null) AmberAccent else TextSecondary
                        )
                    }
                }
                TextButton(onClick = onOpenPrayer) {
                    Text("Detail", color = AmberAccent, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            val prayerDay = state.todayPrayerDay ?: state.selectedPrayerDay
            if (prayerDay != null) {
                val times = prayerDay.times().filter {
                    it.first in listOf("Subuh", "Zuhur", "Asar", "Magrib", "Isya")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    times.forEach { (name, time) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.05f))
                                .padding(vertical = 10.dp, horizontal = 2.dp)
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = time,
                                style = MaterialTheme.typography.titleMedium,
                                color = AmberAccent,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            } else {
                Surface(
                    color = Color.White.copy(alpha = 0.04f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (state.preferences.city == null)
                            "Pilih kota atau aktifkan GPS untuk menampilkan waktu salat"
                        else "Memuat waktu salat...",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun UpdateBanner(update: com.arbdevai.quranvip.data.model.GithubRelease) {
    val context = androidx.compose.ui.platform.LocalContext.current
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color(0xFF1E3A8A).copy(alpha = 0.1f),
        borderColor = Color(0xFF3B82F6).copy(alpha = 0.5f)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.SystemUpdate, contentDescription = null, tint = Color(0xFF60A5FA))
                Spacer(Modifier.width(8.dp))
                Text("Pembaruan Tersedia: ${update.name.ifBlank { update.tag_name }}", 
                    color = Color(0xFF93C5FD), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.height(8.dp))
            Text(update.body.take(150) + if (update.body.length > 150) "..." else "", 
                color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(12.dp))
            
            val downloadUrl = update.assets.firstOrNull { it.name.endsWith(".apk") }?.browser_download_url 
                ?: "https://github.com/arbdevai/alquran-vip/releases/latest"
                
            Button(
                onClick = { 
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(downloadUrl))
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6), contentColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Download & Install Update", fontWeight = FontWeight.Bold)
            }
        }
    }
}
