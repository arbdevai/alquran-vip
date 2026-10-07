package com.arbdevai.quranvip.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbdevai.quranvip.data.model.Reciters
import com.arbdevai.quranvip.ui.AppScreen
import com.arbdevai.quranvip.ui.AppViewModel
import com.arbdevai.quranvip.ui.UiState
import com.arbdevai.quranvip.ui.components.CalmIconTile
import com.arbdevai.quranvip.ui.components.CalmRowItem
import com.arbdevai.quranvip.ui.components.CalmScreenTitle
import com.arbdevai.quranvip.ui.components.CalmSection
import com.arbdevai.quranvip.ui.components.CalmTimeBlock
import com.arbdevai.quranvip.ui.components.QuietIconButton
import com.arbdevai.quranvip.ui.theme.AmberAccent
import com.arbdevai.quranvip.ui.theme.ArabicFontFamily
import com.arbdevai.quranvip.ui.theme.BgCanvas
import com.arbdevai.quranvip.ui.theme.BorderSubtle
import com.arbdevai.quranvip.ui.theme.SurfaceCard
import com.arbdevai.quranvip.ui.theme.SurfaceInput
import com.arbdevai.quranvip.ui.theme.TextPrimary
import com.arbdevai.quranvip.ui.theme.TextSecondary
import java.time.LocalDate
import java.time.format.TextStyle as JavaTextStyle
import java.util.Locale

@Composable
fun PrayerScreen(state: UiState, viewModel: AppViewModel, modifier: Modifier = Modifier) {
    var cityDialog by rememberSaveable { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) viewModel.detectLocation() else viewModel.locationPermissionDenied()
    }

    Column(
        modifier.fillMaxSize().background(BgCanvas).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        CalmScreenTitle(title = "Jadwal salat", subtitle = state.preferences.city?.lokasi ?: "Atur lokasi dulu")
        Spacer(modifier = Modifier.height(14.dp))

        CalmSection(title = "Lokasi", actionLabel = if (state.preferences.city == null) null else "Ganti kota", onAction = { cityDialog = true }) {
            CalmRowItem(
                leading = { CalmIconTile(Icons.Default.MyLocation, AmberAccent) },
                title = state.preferences.city?.lokasi ?: "Lokasi belum dipilih",
                subtitle = if (state.locationError != null) state.locationError else "Koordinat GPS tidak disimpan—hanya dipakai untuk memilih kota.",
                trailing = {
                    TextButton(onClick = {
                        permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                    }) { Text("Gunakan GPS") }
                },
                showDivider = state.preferences.city != null
            )
            if (state.preferences.city == null) {
                CalmRowItem(
                    leading = { CalmIconTile(Icons.Default.LocationCity, TextSecondary) },
                    title = "Pilih kota",
                    subtitle = "Cari kabupaten atau kota",
                    trailing = null,
                    showDivider = false,
                    onClick = { cityDialog = true }
                )
            }
            if (state.locationLoading) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 12.dp)) {
                    CircularProgressIndicator(color = AmberAccent, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(8.dp))
                    Text("Mencari kota terdekat...", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        CalmSection(title = "Hari ini") {
            val selected = state.selectedPrayerDay
            if (state.prayerLoading) {
                Text("Memuat jadwal salat...", color = TextSecondary, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 12.dp))
            } else if (state.prayerError != null) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 10.dp)) {
                    Text(state.prayerError, color = TextSecondary, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                    TextButton(onClick = viewModel::retryPrayer) { Text("Coba lagi") }
                }
            } else if (selected != null) {
                MonthPager(state, viewModel)
                Spacer(modifier = Modifier.height(10.dp))
                CalendarGrid(state, viewModel)
                Spacer(modifier = Modifier.height(12.dp))
                Text("${state.selectedPrayerDate}", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    state.selectedCalendar?.hijr?.let { "${it.day} ${it.monthName} ${it.year} Hijriah" } ?: "Kalender Hijriah dimuat otomatis",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    selected.times().forEach { (name, time) ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(name, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                            Text(time, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                        }
                    }
                }
            } else {
                Text("Pilih kota dulu agar jadwal yang tampil tidak keliru.", color = TextSecondary, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 12.dp))
            }
        }
    }

    if (cityDialog) CityDialog(state, viewModel) { cityDialog = false }
}

@Composable
private fun MonthPager(state: UiState, viewModel: AppViewModel) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        TextButton(onClick = { viewModel.changePrayerMonth(-1) }) { Text("‹ Bulan lalu") }
        Text(
            "${state.prayerMonth.month.getDisplayName(JavaTextStyle.FULL, Locale("id"))} ${state.prayerMonth.year}",
            color = TextPrimary,
            style = MaterialTheme.typography.titleMedium
        )
        TextButton(onClick = { viewModel.changePrayerMonth(1) }) { Text("Bulan depan ›") }
    }
}

@Composable
private fun CalendarGrid(state: UiState, viewModel: AppViewModel) {
    val month = state.prayerMonth
    val lead = (month.atDay(1).dayOfWeek.value % 7)
    Column {
        Row(Modifier.fillMaxWidth()) {
            listOf("Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab").forEach {
                Text(it, color = TextSecondary, textAlign = TextAlign.Center, modifier = Modifier.weight(1f).padding(vertical = 4.dp), fontSize = 12.sp)
            }
        }
        (List(lead) { null } + (1..month.lengthOfMonth()).map { month.atDay(it) }).chunked(7).forEach { week ->
            Row(Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    val selected = date == state.selectedPrayerDate
                    val today = date == LocalDate.now()
                    Box(
                        Modifier.weight(1f).padding(2.dp)
                            .background(if (selected) AmberAccent else Color.Transparent, MaterialTheme.shapes.small)
                            .clickable(enabled = date != null) { date?.let(viewModel::setPrayerDate) }
                            .padding(vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (date != null) {
                            Text("${date.dayOfMonth}", color = if (selected) Color(0xFF141518) else TextPrimary, fontWeight = if (selected || today) FontWeight.SemiBold else FontWeight.Normal)
                        }
                    }
                }
                repeat(7 - week.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun CityDialog(state: UiState, viewModel: AppViewModel, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cari kota", color = TextPrimary) },
        text = {
            Column {
                OutlinedTextField(
                    value = state.cityQuery,
                    onValueChange = viewModel::setCityQuery,
                    singleLine = true,
                    placeholder = { Text("Contoh: Surabaya") },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = TextSecondary) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                when {
                    state.citiesLoading -> Text("Mencari...", color = TextSecondary, modifier = Modifier.padding(12.dp))
                    state.cityResults.isEmpty() && state.cityQuery.isNotBlank() -> Text("Kota tidak ditemukan. Coba ejaan lain.", color = TextSecondary, modifier = Modifier.padding(12.dp))
                    else -> LazyColumn(modifier = Modifier.heightIn(max = 340.dp)) {
                        items(state.cityResults.take(20), key = { it.id }) { city ->
                            Row(
                                Modifier.fillMaxWidth().clickable { viewModel.chooseCity(city, state.preferences.zone); onDismiss() }.padding(vertical = 11.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Place, null, tint = TextSecondary)
                                Spacer(Modifier.width(8.dp))
                                Text(city.lokasi, color = TextPrimary)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Batal") } },
        containerColor = SurfaceCard
    )
}

@Composable
fun BookmarksScreen(state: UiState, viewModel: AppViewModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier.fillMaxSize().background(BgCanvas),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { CalmScreenTitle("Simpanan", "Ayat yang Anda tandai") }
        if (state.preferences.bookmarks.isEmpty()) {
            item { Text("Belum ada ayat tersimpan. Sentuh sebuah ayat saat membaca, lalu pilih Simpan.", color = TextSecondary, style = MaterialTheme.typography.bodySmall) }
        } else {
            items(state.preferences.bookmarks, key = { "${it.surah}:${it.ayah}" }) { bookmark ->
                Surface(color = SurfaceCard, shape = MaterialTheme.shapes.medium, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Surah ${bookmark.name} · ayat ${bookmark.ayah}", color = AmberAccent, style = MaterialTheme.typography.titleMedium)
                            TextButton(onClick = { viewModel.removeOrAddBookmark(bookmark) }) {
                                Icon(Icons.Default.DeleteOutline, null, modifier = Modifier.size(17.dp))
                                Spacer(Modifier.width(3.dp))
                                Text("Hapus")
                            }
                        }
                        Text(bookmark.arabic, fontFamily = ArabicFontFamily, fontSize = 21.sp, lineHeight = 40.sp, color = TextPrimary, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(6.dp))
                        Text(bookmark.translation, color = TextSecondary, style = MaterialTheme.typography.bodySmall, maxLines = 3)
                        TextButton(onClick = { viewModel.openSurah(bookmark.surah, bookmark.ayah) }, modifier = Modifier.align(Alignment.Start)) {
                            Text("Buka di mushaf")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TasbihScreen(state: UiState, viewModel: AppViewModel, modifier: Modifier = Modifier) {
    val presets = listOf("Subhanallah", "Alhamdulillah", "Allahu Akbar", "Astaghfirullah")
    var selected by rememberSaveable { mutableStateOf(presets.first()) }
    Column(
        modifier.fillMaxSize().background(BgCanvas).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CalmScreenTitle("Tasbih", selected, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(14.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            presets.take(3).forEach {
                FilterChip(
                    selected = selected == it,
                    onClick = { selected = it },
                    label = { Text(it, fontSize = 12.sp) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(Modifier.height(34.dp))
        Surface(shape = MaterialTheme.shapes.extraLarge, color = SurfaceCard, modifier = Modifier.size(220.dp).clickable { viewModel.incrementTasbih() }) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
                Text("${state.preferences.tasbih}", fontSize = 58.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Text("Sentuh lingkaran untuk menghitung", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(Modifier.height(22.dp))
        OutlinedButton(onClick = viewModel::resetTasbih) {
            Icon(Icons.Default.Refresh, null, tint = AmberAccent)
            Spacer(Modifier.width(6.dp))
            Text("Mulai dari nol")
        }
    }
}

@Composable
fun SettingsScreen(state: UiState, viewModel: AppViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize().background(BgCanvas).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        CalmScreenTitle("Pengaturan", "Disesuaikan dengan kebiasaan membaca")
        Spacer(Modifier.height(14.dp))

        CalmSection(title = "Tampilan ayat") {
            SettingSwitch("Tampilkan transliterasi", state.preferences.showLatin, viewModel::toggleLatin)
            SettingSwitch("Tampilkan terjemahan", state.preferences.showTranslation, viewModel::toggleTranslation, showDivider = false)
        }
        Spacer(Modifier.height(14.dp))
        CalmSection(title = "Ukuran huruf Arab: ${state.preferences.fontSize}") {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                Text("Ukuran", color = TextSecondary, modifier = Modifier.weight(1f))
                TextButton(onClick = { viewModel.setFontSize(state.preferences.fontSize - 1) }) { Text("Kecilkan") }
                TextButton(onClick = { viewModel.setFontSize(state.preferences.fontSize + 1) }) { Text("Besarkan") }
            }
        }
        Spacer(Modifier.height(14.dp))
        CalmSection(title = "Qari") {
            var expanded by rememberSaveable { mutableStateOf(false) }
            CalmRowItem(
                title = Reciters.names[state.preferences.qori] ?: "Pilih qari",
                subtitle = "Suara untuk audio ayat",
                trailing = { TextButton(onClick = { expanded = true }) { Text("Ganti") } },
                showDivider = false
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                Reciters.names.forEach { (id, name) ->
                    DropdownMenuItem(text = { Text(name) }, onClick = { viewModel.setQori(id); expanded = false })
                }
            }
        }
        Spacer(Modifier.height(14.dp))
        CalmSection(title = "Wilayah waktu salat") {
            val zones = listOf(
                "Asia/Jakarta" to "WIB (Sumatera, Jawa, Kalimantan Barat/Tengah)",
                "Asia/Makassar" to "WITA (Bali, Kalimantan Timur/Selatan, Sulawesi)",
                "Asia/Jayapura" to "WIT (Maluku, Papua)"
            )
            zones.forEach { (id, label) ->
                CalmRowItem(
                    title = label,
                    subtitle = id,
                    trailing = { if (state.preferences.zone == id) Icon(Icons.Default.Check, null, tint = AmberAccent) },
                    showDivider = id != zones.last().first,
                    onClick = { viewModel.setZone(id) }
                )
            }
        }
    }
}

@Composable
private fun SettingSwitch(label: String, checked: Boolean, onClick: () -> Unit, showDivider: Boolean = true) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = TextPrimary, modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = { onClick() },
            colors = SwitchDefaults.colors(checkedThumbColor = AmberAccent, checkedTrackColor = SurfaceInput)
        )
    }
    if (showDivider) androidx.compose.material3.HorizontalDivider(color = BorderSubtle, thickness = 0.5.dp)
}

@Composable
private fun FilterChip(selected: Boolean, onClick: () -> Unit, label: @Composable () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        color = if (selected) AmberAccent else SurfaceInput,
        shape = MaterialTheme.shapes.small,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Box(Modifier.padding(horizontal = 8.dp, vertical = 8.dp), contentAlignment = Alignment.Center) {
            val contentColor = if (selected) Color(0xFF141518) else TextPrimary
            androidx.compose.runtime.CompositionLocalProvider(androidx.compose.material3.LocalContentColor provides contentColor, content = label)
        }
    }
}
