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
import com.arbdevai.quranvip.data.model.City
import com.arbdevai.quranvip.data.model.Reciters
import com.arbdevai.quranvip.ui.AppViewModel
import com.arbdevai.quranvip.ui.UiState
import com.arbdevai.quranvip.ui.theme.*
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun BookmarksScreen(state: UiState, viewModel: AppViewModel, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().background(BgCanvas).padding(Spacing.screenPadding)) {
        Text("Bookmark", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
        Spacer(Modifier.height(12.dp))
        if (state.preferences.bookmarks.isEmpty()) {
            EmptyView("Belum ada ayat yang disimpan")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.preferences.bookmarks, key = { "${it.surah}:${it.ayah}" }) { bookmark ->
                    Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), modifier = Modifier.fillMaxWidth().clickable { viewModel.openSurah(bookmark.surah, bookmark.ayah) }) {
                        Column(Modifier.padding(14.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("${bookmark.name} : ${bookmark.ayah}", style = MaterialTheme.typography.titleMedium, color = AmberAccent)
                                IconButton(onClick = { viewModel.removeOrAddBookmark(bookmark) }) { Icon(Icons.Default.DeleteOutline, "Hapus", tint = TextSecondary) }
                            }
                            Text(bookmark.arabic, style = MaterialTheme.typography.bodyLarge, color = TextPrimary, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())
                            Spacer(Modifier.height(4.dp))
                            Text(bookmark.translation, style = MaterialTheme.typography.bodySmall, color = TextSecondary, maxLines = 2)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TasbihScreen(state: UiState, viewModel: AppViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgCanvas)
            .verticalScroll(rememberScrollState())
            .padding(Spacing.screenPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tasbih Digital", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
        Spacer(Modifier.height(36.dp))
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = SurfaceCard,
            modifier = Modifier.size(220.dp).clickable { viewModel.incrementTasbih() }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(Icons.Default.TouchApp, null, tint = CatTasbih, modifier = Modifier.size(42.dp))
                Spacer(Modifier.height(12.dp))
                Text("${state.preferences.tasbih}", style = MaterialTheme.typography.displayLarge, color = AmberAccent)
                Text("Ketuk untuk menghitung", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }
        Spacer(Modifier.height(24.dp))
        OutlinedButton(onClick = viewModel::resetTasbih) {
            Icon(Icons.Default.Refresh, contentDescription = null, tint = AmberAccent)
            Spacer(Modifier.width(6.dp))
            Text("Reset Hitungan", color = AmberAccent)
        }
    }
}

@Composable
fun SettingsScreen(state: UiState, viewModel: AppViewModel, modifier: Modifier = Modifier) {
    var zone by rememberSaveable(state.preferences.zone) { mutableStateOf(state.preferences.zone) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgCanvas)
            .verticalScroll(rememberScrollState())
            .padding(Spacing.screenPadding)
    ) {
        Text("Pengaturan", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
        Spacer(Modifier.height(16.dp))
        Text("Tampilan Bacaan", style = MaterialTheme.typography.titleMedium, color = AmberAccent)
        Spacer(Modifier.height(6.dp))
        SettingSwitch("Tampilkan Latin", state.preferences.showLatin, viewModel::toggleLatin)
        SettingSwitch("Tampilkan Terjemahan", state.preferences.showTranslation, viewModel::toggleTranslation)
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Text("Ukuran Huruf Arab: ${state.preferences.fontSize} sp", color = TextPrimary, modifier = Modifier.weight(1f))
            IconButton(onClick = { viewModel.setFontSize(state.preferences.fontSize - 1) }) { Text("A−", color = AmberAccent, fontWeight = FontWeight.Bold) }
            IconButton(onClick = { viewModel.setFontSize(state.preferences.fontSize + 1) }) { Text("A+", color = AmberAccent, fontWeight = FontWeight.Bold) }
        }
        Spacer(Modifier.height(16.dp))
        Text("Qari Pilihan", style = MaterialTheme.typography.titleMedium, color = AmberAccent)
        Spacer(Modifier.height(6.dp))
        var expanded by rememberSaveable { mutableStateOf(false) }
        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(Reciters.names[state.preferences.qori] ?: "Pilih Qari", color = TextPrimary)
                Icon(Icons.Default.ArrowDropDown, null, tint = TextSecondary)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                Reciters.names.forEach { (id, name) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = { viewModel.setQori(id); expanded = false }
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Text("Zona Waktu Jadwal Shalat", style = MaterialTheme.typography.titleMedium, color = AmberAccent)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = zone,
            onValueChange = { zone = it },
            singleLine = true,
            label = { Text("Zona Waktu (IANA)") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedBorderColor = AmberAccent
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(6.dp))
        Button(
            onClick = { viewModel.setZone(zone) },
            colors = ButtonDefaults.buttonColors(containerColor = AmberAccent, contentColor = Color.Black)
        ) {
            Text("Simpan Zona Waktu")
        }
    }
}

@Composable
private fun SettingSwitch(label: String, checked: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = TextPrimary, modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = { onClick() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = AmberAccent,
                checkedTrackColor = SurfacePill
            )
        )
    }
}

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
        modifier = modifier
            .fillMaxSize()
            .background(BgCanvas)
            .verticalScroll(rememberScrollState())
            .padding(Spacing.screenPadding)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("Jadwal Shalat", style = MaterialTheme.typography.titleLarge, color = TextPrimary, modifier = Modifier.weight(1f))
            IconButton(onClick = { cityDialog = true }) {
                Icon(Icons.Default.LocationCity, "Pilih Kota", tint = AmberAccent)
            }
            IconButton(onClick = {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }) {
                Icon(Icons.Default.MyLocation, "Gunakan GPS", tint = CatJadwal)
            }
        }

        Text(
            text = state.preferences.city?.let { "${it.lokasi} (${state.preferences.zone})" } ?: "Kota belum dipilih",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        if (state.locationLoading) {
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(color = AmberAccent, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
                Text("Mendeteksi lokasi...", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }

        if (state.locationError != null) {
            Spacer(Modifier.height(6.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(state.locationError, style = MaterialTheme.typography.bodySmall, color = Color(0xFFFF6B6B), modifier = Modifier.weight(1f))
                    TextButton(onClick = viewModel::detectLocation) { Text("Coba Lagi", color = AmberAccent) }
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // Month Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.changePrayerMonth(-1) }) {
                Icon(Icons.Default.ChevronLeft, "Bulan Lalu", tint = TextPrimary)
            }
            Text(
                text = "${state.prayerMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${state.prayerMonth.year}",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            IconButton(onClick = { viewModel.changePrayerMonth(1) }) {
                Icon(Icons.Default.ChevronRight, "Bulan Depan", tint = TextPrimary)
            }
        }

        // Hijri Conversion Status
        when {
            state.calendarLoading -> {
                Text("Memuat kalender Hijriah...", color = TextSecondary, style = MaterialTheme.typography.bodySmall, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), textAlign = TextAlign.Center)
            }
            state.calendarError != null -> {
                Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Text("Gagal memuat Hijriah", color = Color(0xFFFF6B6B), style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.width(6.dp))
                    TextButton(onClick = viewModel::retryCalendar) { Text("Coba Lagi", color = AmberAccent, style = MaterialTheme.typography.bodySmall) }
                }
            }
            state.selectedCalendar != null -> {
                val cal = state.selectedCalendar
                Text(
                    text = "${cal.hijr.day} ${cal.hijr.monthName} ${cal.hijr.year} H",
                    color = AmberAccent,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(8.dp))
        CalendarGrid(state, viewModel)
        Spacer(Modifier.height(14.dp))

        // Prayer Schedule Card
        when {
            state.prayerLoading -> {
                Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AmberAccent)
                }
            }
            state.prayerError != null -> {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(state.prayerError, color = TextSecondary, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(10.dp))
                        Button(onClick = viewModel::retryPrayer, colors = ButtonDefaults.buttonColors(containerColor = AmberAccent, contentColor = Color.Black)) {
                            Text("Coba Lagi")
                        }
                    }
                }
            }
            state.preferences.city == null -> {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.LocationOff, null, tint = AmberAccent, modifier = Modifier.size(36.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("Pilih kota atau aktifkan GPS untuk menampilkan jadwal shalat", color = TextSecondary, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = { cityDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = AmberAccent, contentColor = Color.Black)) {
                            Text("Pilih Kota")
                        }
                    }
                }
            }
            state.selectedPrayerDay != null -> {
                val day = requireNotNull(state.selectedPrayerDay)
                Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Jadwal Shalat: ${state.selectedPrayerDate}", color = AmberAccent, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(10.dp))
                        day.times().forEach { (name, time) ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(name, color = TextSecondary)
                                Text(time, color = TextPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            else -> {
                Text(
                    text = "Jadwal untuk tanggal ini belum tersedia",
                    color = TextSecondary,
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    if (cityDialog) CityDialog(state, viewModel) { cityDialog = false }
}

@Composable
private fun CalendarGrid(state: UiState, viewModel: AppViewModel) {
    val month = state.prayerMonth
    val first = month.atDay(1)
    val lead = first.dayOfWeek.value % 7

    Column {
        Row(Modifier.fillMaxWidth()) {
            listOf("Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab").forEach { dayLabel ->
                Text(
                    text = dayLabel,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.weight(1f).padding(vertical = 4.dp)
                )
            }
        }
        val cells = List(lead) { null } + (1..month.lengthOfMonth()).map { month.atDay(it) }
        cells.chunked(7).forEach { week ->
            Row(Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    val isSelected = date == state.selectedPrayerDate
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(2.dp)
                            .height(36.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(
                                when {
                                    isSelected -> AmberAccent
                                    date != null -> SurfaceCard
                                    else -> Color.Transparent
                                }
                            )
                            .clickable(enabled = date != null) { date?.let(viewModel::setPrayerDate) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (date != null) {
                            Text(
                                text = "${date.dayOfMonth}",
                                color = if (isSelected) Color.Black else TextPrimary,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
                repeat(7 - week.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun CityDialog(state: UiState, viewModel: AppViewModel, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pilih Kota", color = TextPrimary) },
        text = {
            Column {
                OutlinedTextField(
                    value = state.cityQuery,
                    onValueChange = viewModel::setCityQuery,
                    singleLine = true,
                    label = { Text("Cari nama kota/kabupaten") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                if (state.citiesLoading) {
                    Box(Modifier.fillMaxWidth().padding(12.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = AmberAccent, modifier = Modifier.size(24.dp))
                    }
                }
                Spacer(Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.heightIn(max = 240.dp)) {
                    items(state.cityResults.take(15)) { city ->
                        Text(
                            text = city.lokasi,
                            color = TextPrimary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.chooseCity(city, state.preferences.zone)
                                    onDismiss()
                                }
                                .padding(vertical = 10.dp, horizontal = 4.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Tutup", color = AmberAccent) }
        },
        containerColor = SurfaceCard
    )
}
