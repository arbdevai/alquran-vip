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
fun HomeScreen(
    state: UiState,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BgCanvas),
        contentPadding = PaddingValues(Spacing.screenPadding),
        verticalArrangement = Arrangement.spacedBy(Spacing.itemSpacing)
    ) {
        // 1. Hero Card (Beranda)
        item {
            val hijriText = state.selectedCalendar?.hijr?.let {
                "${it.day} ${it.monthName} ${it.year} H"
            } ?: "Al-Qur'an & Jadwal Shalat Digital"
            QuranHeroBanner(
                title = "Al-Qur'an VIP",
                subtitle = hijriText,
                tag = "BISMILLAHIRRAHMANIRRAHIM",
                badges = listOf("114 Surah", "30 Juz", state.preferences.city?.lokasi ?: "Jadwal Shalat"),
                trailingContent = {
                    FilledIconButton(
                        onClick = { viewModel.setScreen(AppScreen.SETTINGS) },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Color.White.copy(alpha = 0.2f),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(Icons.Default.Tune, contentDescription = "Pengaturan")
                    }
                }
            )
        }

        // 2. Terakhir Dibaca Card (Last Read)
        item {
            val lastRead = state.preferences.lastRead
            if (lastRead != null) {
                LastReadCard(
                    lastRead = lastRead,
                    onClick = { viewModel.openSurah(lastRead.surah, lastRead.ayah) }
                )
            }
        }

        // 3. Quick Menu Navigation Grid (Glassmorphism 2026)
        item {
            Text(
                text = "Fitur Utama",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MenuGridItem(
                    title = "Al-Qur'an",
                    icon = Icons.Default.MenuBook,
                    iconTint = CatQuran,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setScreen(AppScreen.QURAN) }
                )
                MenuGridItem(
                    title = "Jadwal",
                    icon = Icons.Default.AccessTime,
                    iconTint = CatJadwal,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setScreen(AppScreen.PRAYER) }
                )
                MenuGridItem(
                    title = "Tasbih",
                    icon = Icons.Default.TouchApp,
                    iconTint = CatTasbih,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setScreen(AppScreen.TASBIH) }
                )
                MenuGridItem(
                    title = "Bookmark",
                    icon = Icons.Default.Bookmark,
                    iconTint = CatTahlil,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setScreen(AppScreen.BOOKMARKS) }
                )
            }
        }

        // 4. Jadwal Shalat Ringkas Hari Ini (Glassmorphism Widget)
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
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.cardPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = null,
                        tint = AmberAccent,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Terakhir Dibaca",
                        style = MaterialTheme.typography.labelSmall,
                        color = AmberAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Surah ${lastRead.name}",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Ayat ke-${lastRead.ayah}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
            FilledIconButton(
                onClick = onClick,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = AmberAccent,
                    contentColor = Color.Black
                ),
                modifier = Modifier.size(40.dp)
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
private fun MenuGridItem(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = TextPrimary,
                fontWeight = FontWeight.Medium,
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
        onClick = onOpenPrayer
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(CatJadwal.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = CatJadwal,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Jadwal Shalat Hari Ini",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        val cityLokasi = state.preferences.city?.lokasi ?: "Sentuh untuk pilih kota"
                        Text(
                            text = cityLokasi,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (state.preferences.city != null) AmberAccent else TextSecondary
                        )
                    }
                }
                TextButton(onClick = onOpenPrayer) {
                    Text("Detail", color = AmberAccent, style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            val prayerDay = state.selectedPrayerDay
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
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(alpha = 0.05f))
                                .padding(vertical = 8.dp, horizontal = 2.dp)
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = time,
                                style = MaterialTheme.typography.bodyMedium,
                                color = AmberAccent,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            } else {
                Surface(
                    color = Color.White.copy(alpha = 0.04f),
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (state.preferences.city == null)
                            "Pilih kota lokasi Anda untuk menampilkan jadwal shalat otomatis"
                        else "Memuat waktu shalat hari ini...",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}
