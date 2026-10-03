package com.arbdevai.quranvip.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
        // 1. Hero Banner
        item {
            val hijriText = state.selectedCalendar?.hijr?.let {
                "${it.day} ${it.monthName} ${it.year} H"
            } ?: "Al-Qur'an & Jadwal Shalat Digital"
            QuranHeroBanner(
                title = "Al-Qur'an VIP",
                subtitle = hijriText,
                tag = "BISMILLAHIRRAHMANIRRAHIM"
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

        // 3. Quick Menu Navigation Grid
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

        // 4. Jadwal Shalat Ringkas Hari Ini (Today's Prayer Widget)
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
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
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
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = SurfaceCard,
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.15f)),
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
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenPrayer)
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
                    Icon(
                        imageVector = Icons.Default.Mosque,
                        contentDescription = null,
                        tint = CatJadwal,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Jadwal Shalat Hari Ini",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        val cityLokasi = state.preferences.city?.lokasi ?: "Belum Memilih Kota"
                        Text(
                            text = cityLokasi,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }
                TextButton(onClick = onOpenPrayer) {
                    Text("Lihat Kalender", color = AmberAccent, style = MaterialTheme.typography.labelSmall)
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
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    times.forEach { (name, time) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceInput)
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
                                style = MaterialTheme.typography.titleMedium,
                                color = AmberAccent,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (name != "Isya") {
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                    }
                }
            } else if (state.preferences.city == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceInput)
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Pilih kota untuk menampilkan jadwal shalat harian",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            } else if (state.prayerLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AmberAccent, modifier = Modifier.size(24.dp))
                }
            } else {
                Text(
                    text = "Ketuk untuk memuat jadwal shalat",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
            }
        }
    }
}
