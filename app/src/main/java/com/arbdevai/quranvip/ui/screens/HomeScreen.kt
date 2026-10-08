package com.arbdevai.quranvip.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
        // 1. Signature Emerald Hero Banner (Only on Home screen)
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

        // 2. Terakhir Dibaca Widget (Last Read)
        state.preferences.lastRead?.let { lastRead ->
            item {
                LastReadCard(
                    lastRead = lastRead,
                    onClick = { viewModel.openSurah(lastRead.surah, lastRead.ayah) }
                )
            }
        }

        // 3. 2-Column Menu Grid (Clean Human Copy, No AI Fluff)
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

        // 4. Jadwal Shalat Hari Ini (Interactive 5 times chips)
        item {
            TodayPrayerCard(
                state = state,
                onOpenPrayer = { viewModel.setScreen(AppScreen.PRAYER) }
            )
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
