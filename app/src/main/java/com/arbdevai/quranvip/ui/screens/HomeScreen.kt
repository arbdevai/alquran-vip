package com.arbdevai.quranvip.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arbdevai.quranvip.data.model.ReadingPosition
import com.arbdevai.quranvip.ui.AppScreen
import com.arbdevai.quranvip.ui.AppViewModel
import com.arbdevai.quranvip.ui.UiState
import com.arbdevai.quranvip.ui.components.CalmIconTile
import com.arbdevai.quranvip.ui.components.CalmRowItem
import com.arbdevai.quranvip.ui.components.CalmScreenTitle
import com.arbdevai.quranvip.ui.components.CalmSection
import com.arbdevai.quranvip.ui.components.CalmTimeBlock
import com.arbdevai.quranvip.ui.components.QuietIconButton
import com.arbdevai.quranvip.ui.components.QuranHeroBanner
import com.arbdevai.quranvip.ui.theme.AmberAccent
import com.arbdevai.quranvip.ui.theme.BgCanvas
import com.arbdevai.quranvip.ui.theme.CatJadwal
import com.arbdevai.quranvip.ui.theme.SurfaceCard
import com.arbdevai.quranvip.ui.theme.TextPrimary
import com.arbdevai.quranvip.ui.theme.TextSecondary

@Composable
fun HomeScreen(state: UiState, viewModel: AppViewModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize().background(BgCanvas),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                CalmScreenTitle(
                    title = "Assalamu'alaikum",
                    subtitle = state.selectedCalendar?.hijr?.today ?: "Semoga hari ini diberkahi",
                    modifier = Modifier.weight(1f)
                )
                QuietIconButton(Icons.Default.Tune, "Pengaturan", onClick = { viewModel.setScreen(AppScreen.SETTINGS) })
            }
        }

        // Home alone earns a modest hero because it gives a useful at-a-glance prayer context.
        item {
            val city = state.preferences.city?.lokasi ?: "Pilih lokasi untuk jadwal salat"
            val next = state.selectedPrayerDay?.times()?.firstOrNull { it.first == "Subuh" }
            QuranHeroBanner(
                title = next?.let { "${it.first} · ${it.second}" } ?: "Waktu salat hari ini",
                subtitle = city,
                tag = "JADWAL SALAT",
                badges = if (state.preferences.city == null) listOf("Gunakan GPS atau pilih kota") else listOf("Lihat jadwal lengkap"),
                trailingContent = {
                    QuietIconButton(
                        icon = Icons.Default.AccessTime,
                        contentDescription = "Buka jadwal salat",
                        onClick = { viewModel.setScreen(AppScreen.PRAYER) },
                        tint = Color.White
                    )
                }
            )
        }

        state.preferences.lastRead?.let { lastRead ->
            item {
                CalmSection(title = "Lanjutkan membaca") {
                    CalmRowItem(
                        leading = { CalmIconTile(Icons.Default.MenuBook, AmberAccent) },
                        title = lastRead.name,
                        subtitle = "Ayat ${lastRead.ayah}",
                        trailing = { Icon(Icons.Default.ArrowForward, null, tint = TextSecondary) },
                        showDivider = false,
                        onClick = { viewModel.openSurah(lastRead.surah, lastRead.ayah) }
                    )
                }
            }
        }

        item {
            CalmSection(title = "Mulai dari sini") {
                CalmRowItem(
                    leading = { CalmIconTile(Icons.Default.MenuBook, AmberAccent) },
                    title = "Baca Al-Qur'an",
                    subtitle = "Cari dan pilih salah satu dari 114 surah",
                    trailing = { Icon(Icons.Default.ArrowForward, null, tint = TextSecondary) },
                    onClick = { viewModel.setScreen(AppScreen.QURAN) }
                )
                CalmRowItem(
                    leading = { CalmIconTile(Icons.Default.AccessTime, CatJadwal) },
                    title = "Jadwal salat",
                    subtitle = if (state.preferences.city == null) "Atur lokasi Anda" else state.preferences.city.lokasi,
                    trailing = { Icon(Icons.Default.ArrowForward, null, tint = TextSecondary) },
                    showDivider = false,
                    onClick = { viewModel.setScreen(AppScreen.PRAYER) }
                )
            }
        }

        item {
            CalmSection(title = "Waktu salat hari ini", actionLabel = "Lihat semua", onAction = { viewModel.setScreen(AppScreen.PRAYER) }) {
                val day = state.selectedPrayerDay
                if (day == null) {
                    Text(
                        if (state.preferences.city == null) "Pilih kota untuk melihat jadwal salat yang akurat." else "Memuat jadwal salat...",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                } else {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        day.times().filter { it.first in listOf("Subuh", "Zuhur", "Asar", "Magrib", "Isya") }.forEach { (name, time) ->
                            CalmTimeBlock(name, time, false, Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
