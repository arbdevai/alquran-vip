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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbdevai.quranvip.data.model.City
import com.arbdevai.quranvip.data.model.Reciters
import com.arbdevai.quranvip.ui.AppScreen
import com.arbdevai.quranvip.ui.AppViewModel
import com.arbdevai.quranvip.ui.UiState
import com.arbdevai.quranvip.ui.components.GlassCard
import com.arbdevai.quranvip.ui.components.QuranHeroBanner
import com.arbdevai.quranvip.ui.theme.*
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
        modifier = modifier
            .fillMaxSize()
            .background(BgCanvas)
            .verticalScroll(rememberScrollState())
            .padding(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 24.dp)
    ) {
        // 1. Signature Hero Card Jadwal Salat
        val cityLokasi = state.preferences.city?.let { "${it.lokasi} (${state.preferences.zone})" } ?: "Lokasi GPS Otomatis"
        val hijriSummary = state.selectedCalendar?.hijr?.let { "${it.day} ${it.monthName} ${it.year} H" } ?: "Jadwal Waktu Salat"
        QuranHeroBanner(
            title = "Jadwal Salat Harian",
            subtitle = "$cityLokasi · $hijriSummary",
            tag = "API MUSLIM V3",
            arabicWatermark = "۞",
            badges = listOf(state.preferences.city?.lokasi ?: "Lokasi GPS", state.preferences.zone, "Kemenag RI"),
            trailingContent = {
                FilledIconButton(
                    onClick = { cityDialog = true },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = Color.Black.copy(alpha = 0.35f),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(Icons.Default.LocationCity, contentDescription = "Pilih Kota")
                }
                Spacer(modifier = Modifier.width(6.dp))
                FilledIconButton(
                    onClick = {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = Color.Black.copy(alpha = 0.35f),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(Icons.Default.MyLocation, contentDescription = "GPS Otomatis")
                }
            }
        )

        if (state.locationLoading) {
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(color = AmberAccent, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
                Text("Mendeteksi koordinat GPS...", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }

        if (state.locationError != null) {
            Spacer(Modifier.height(10.dp))
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        state.locationError,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFF6B6B),
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = viewModel::detectLocation) {
                        Text("Coba Lagi", color = AmberAccent)
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Tombol Pintas ke Kalender Hijriyah Terpisah
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            onClick = { viewModel.setScreen(AppScreen.CALENDAR) }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(CatQuran.copy(alpha = 0.20f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = CatQuran,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Buka Kalender Hijriyah",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Konversi penanggalan & jadwal bulanan",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = AmberAccent,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Today's Detailed Prayer Times
        val day = state.todayPrayerDay ?: state.selectedPrayerDay
        when {
            state.prayerLoading -> {
                LoadingView("Memuat jadwal salat...")
            }
            state.prayerError != null -> {
                ErrorView(state.prayerError, viewModel::retryPrayer)
            }
            day != null -> {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Waktu Salat Hari Ini",
                                color = AmberAccent,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                day.tanggal,
                                color = TextSecondary,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                        Spacer(Modifier.height(14.dp))
                        day.times().forEach { (name, time) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    name,
                                    color = if (name in listOf("Subuh", "Zuhur", "Asar", "Magrib", "Isya")) TextPrimary else TextSecondary,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (name in listOf("Subuh", "Zuhur", "Asar", "Magrib", "Isya")) FontWeight.Bold else FontWeight.Normal
                                )
                                Text(
                                    time,
                                    color = AmberAccent,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            HorizontalDivider(color = BorderSubtle, thickness = 0.5.dp)
                        }
                    }
                }
            }
            else -> {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = if (state.preferences.city == null)
                            "Pilih kota lokasi Anda atau aktifkan GPS untuk melihat jadwal salat akurat"
                        else "Jadwal salat belum tersedia",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }
        }
    }

    if (cityDialog) {
        CityDialog(state, viewModel) { cityDialog = false }
    }
}

@Composable
fun CalendarScreen(state: UiState, viewModel: AppViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgCanvas)
            .verticalScroll(rememberScrollState())
            .padding(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 24.dp)
    ) {
        // Hero Card Kalender Hijriyah & Masehi
        val hijriText = state.selectedCalendar?.hijr?.let { "${it.day} ${it.monthName} ${it.year} H" } ?: "Kalender Hijriyah"
        QuranHeroBanner(
            title = "Kalender Hijriyah & Masehi",
            subtitle = hijriText,
            tag = "PENANGGALAN ISLAM",
            arabicWatermark = "۞",
            badges = listOf(state.selectedCalendar?.ce?.today ?: "Masehi", state.preferences.zone)
        )

        Spacer(Modifier.height(18.dp))

        // Month Selector (Bulan Lalu / Bulan Depan)
        val indonesianMonths = listOf(
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
        )
        val monthName = indonesianMonths.getOrElse(state.prayerMonth.monthValue - 1) { state.prayerMonth.month.name }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.changePrayerMonth(-1) }) {
                Icon(Icons.Default.ChevronLeft, "Bulan Lalu", tint = TextPrimary)
            }
            Text(
                text = "$monthName ${state.prayerMonth.year}",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { viewModel.changePrayerMonth(1) }) {
                Icon(Icons.Default.ChevronRight, "Bulan Depan", tint = TextPrimary)
            }
        }

        // Hijri Conversion Status
        when {
            state.calendarLoading -> {
                Text(
                    "Memuat kalender Hijriah...",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    textAlign = TextAlign.Center
                )
            }
            state.calendarError != null -> {
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(state.calendarError, color = Color(0xFFFF6B6B), style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.width(6.dp))
                    TextButton(onClick = viewModel::retryCalendar) { Text("Coba Lagi", color = AmberAccent, fontSize = 11.sp) }
                }
            }
            state.selectedCalendar != null -> {
                Text(
                    text = "${state.selectedCalendar.hijr.day} ${state.selectedCalendar.hijr.monthName} ${state.selectedCalendar.hijr.year} H",
                    color = AmberAccent,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Interactive Calendar Grid
        CalendarGrid(state, viewModel)

        Spacer(Modifier.height(18.dp))

        // Prayer Schedule for Selected Calendar Date
        val selectedDay = state.selectedPrayerDay
        if (selectedDay != null) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text(
                        "Jadwal Salat: ${selectedDay.tanggal}",
                        color = AmberAccent,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))
                    selectedDay.times().forEach { (name, time) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(name, color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                            Text(time, color = TextPrimary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        HorizontalDivider(color = BorderSubtle, thickness = 0.5.dp)
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarGrid(state: UiState, viewModel: AppViewModel) {
    val month = state.prayerMonth
    val first = month.atDay(1)
    val lead = (first.dayOfWeek.value % 7)

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(Modifier.padding(10.dp)) {
            Row(Modifier.fillMaxWidth()) {
                listOf("Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab").forEach { dayLabel ->
                    Text(
                        text = dayLabel,
                        color = if (dayLabel == "Jum") AmberAccent else TextSecondary,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f).padding(vertical = 4.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            val cells = List(lead) { null } + (1..month.lengthOfMonth()).map { month.atDay(it) }
            cells.chunked(7).forEach { week ->
                Row(Modifier.fillMaxWidth()) {
                    week.forEach { date ->
                        val isSelected = date == state.selectedPrayerDate
                        val isToday = date == LocalDate.now()
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(2.dp)
                                .height(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    when {
                                        isSelected -> AmberAccent
                                        isToday -> Color.White.copy(alpha = 0.15f)
                                        else -> Color.Transparent
                                    }
                                )
                                .clickable(enabled = date != null) {
                                    date?.let(viewModel::setPrayerDate)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (date != null) {
                                Text(
                                    text = "${date.dayOfMonth}",
                                    color = if (isSelected) Color.Black else TextPrimary,
                                    fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
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
}

@Composable
private fun CityDialog(state: UiState, viewModel: AppViewModel, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Pilih Kota / Kabupaten", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            ) {
                OutlinedTextField(
                    value = state.cityQuery,
                    onValueChange = viewModel::setCityQuery,
                    placeholder = { Text("Ketik nama kota, misal: Kediri, Jakarta...", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = AmberAccent) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
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

                Spacer(Modifier.height(10.dp))

                when {
                    state.citiesLoading -> {
                        Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = AmberAccent, modifier = Modifier.size(24.dp))
                        }
                    }
                    state.cityResults.isEmpty() && state.cityQuery.isNotBlank() -> {
                        Text(
                            "Tidak ada kota dengan nama \"${state.cityQuery}\"",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    else -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth().weight(1f, fill = false)
                        ) {
                            items(state.cityResults.take(20), key = { it.id }) { city ->
                                Surface(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.chooseCity(city, state.preferences.zone)
                                            onDismiss()
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Place, null, tint = AmberAccent, modifier = Modifier.size(18.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text(city.lokasi, color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Batal", color = AmberAccent) }
        },
        containerColor = SurfaceCard
    )
}

@Composable
fun BookmarksScreen(state: UiState, viewModel: AppViewModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BgCanvas),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Hero Card Bookmark (Yasin style with ۞)
        item {
            QuranHeroBanner(
                title = "Ayat Favorit",
                subtitle = "${state.preferences.bookmarks.size} Ayat Tersimpan",
                tag = "TADARUS PRIBADI",
                arabicWatermark = "۞",
                badges = listOf("${state.preferences.bookmarks.size} Disimpan", "Tadarus Harian")
            )
        }

        if (state.preferences.bookmarks.isEmpty()) {
            item {
                EmptyView("Belum ada ayat yang disimpan ke bookmark")
            }
        } else {
            items(state.preferences.bookmarks, key = { "${it.surah}:${it.ayah}" }) { bookmark ->
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    onClick = { viewModel.openSurah(bookmark.surah, bookmark.ayah) }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Surah ${bookmark.name} · Ayat ${bookmark.ayah}",
                                style = MaterialTheme.typography.titleMedium,
                                color = AmberAccent,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = { viewModel.removeOrAddBookmark(bookmark) }) {
                                Icon(Icons.Default.DeleteOutline, "Hapus", tint = TextSecondary)
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = bookmark.arabic,
                            color = TextPrimary,
                            textAlign = TextAlign.End,
                            style = TextStyle(
                                fontFamily = ArabicFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 22.sp,
                                lineHeight = 42.sp,
                                lineHeightStyle = ArabicLineHeightStyle,
                                textDirection = TextDirection.Rtl,
                                platformStyle = PlatformTextStyle(includeFontPadding = false)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = bookmark.translation,
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.bodySmall.copy(
                                textDirection = TextDirection.Ltr,
                                lineHeight = 22.sp
                            ),
                            color = TextTranslation,
                            maxLines = 3
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TasbihScreen(state: UiState, viewModel: AppViewModel, modifier: Modifier = Modifier) {
    val zikirPresets = listOf(
        "Subhanallah",
        "Alhamdulillah",
        "Allahu Akbar",
        "Astaghfirullah",
        "La ilaha illallah"
    )
    var selectedZikir by rememberSaveable { mutableStateOf(zikirPresets.first()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgCanvas)
            .verticalScroll(rememberScrollState())
            .padding(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Hero Card Tasbih (Yasin style with ۞)
        QuranHeroBanner(
            title = "Tasbih Digital VIP",
            subtitle = selectedZikir,
            tag = "ZIKIR HARIAN",
            arabicWatermark = "۞",
            badges = listOf("Haptic Touch", "Target 33x / 99x", "Hitungan: ${state.preferences.tasbih}")
        )

        Spacer(Modifier.height(20.dp))

        // Preset Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            zikirPresets.take(3).forEach { zikir ->
                val isSelected = selectedZikir == zikir
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedZikir = zikir },
                    label = { Text(zikir, fontSize = 11.5.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AmberAccent,
                        selectedLabelColor = Color.Black
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(36.dp))

        // Glowing Big Tactile Ring Button
        GlassCard(
            shape = CircleShape,
            containerColor = SurfaceCard,
            borderColor = AmberAccent.copy(alpha = 0.50f),
            borderWidth = 2.dp,
            modifier = Modifier
                .size(240.dp)
                .clickable { viewModel.incrementTasbih() }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(CatTasbih.copy(alpha = 0.20f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.TouchApp,
                        contentDescription = null,
                        tint = CatTasbih,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(Modifier.height(14.dp))
                Text(
                    text = "${state.preferences.tasbih}",
                    fontSize = 58.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberAccent
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Ketuk untuk berdzikir",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        OutlinedButton(
            onClick = viewModel::resetTasbih,
            shape = RoundedCornerShape(20.dp)
        ) {
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
            .padding(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 24.dp)
    ) {
        // 1. Hero Card Settings
        val qoriName = Reciters.names[state.preferences.qori] ?: "Qari Pilihan"
        QuranHeroBanner(
            title = "Pengaturan VIP",
            subtitle = "Preferensi Bacaan, Qari, & Waktu Salat",
            tag = "PREFERENSI APLIKASI",
            arabicWatermark = "۞",
            badges = listOf(qoriName, "${state.preferences.fontSize} sp", state.preferences.zone)
        )

        Spacer(Modifier.height(18.dp))

        Text("Tampilan Bacaan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AmberAccent)
        Spacer(Modifier.height(8.dp))
        SettingSwitch("Tampilkan Latin", state.preferences.showLatin, viewModel::toggleLatin)
        SettingSwitch("Tampilkan Terjemahan", state.preferences.showTranslation, viewModel::toggleTranslation)

        GlassCard(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(14.dp)
            ) {
                Text(
                    "Ukuran Huruf Arab: ${state.preferences.fontSize} sp",
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { viewModel.setFontSize(state.preferences.fontSize - 1) }) {
                    Text("A−", color = AmberAccent, fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = { viewModel.setFontSize(state.preferences.fontSize + 1) }) {
                    Text("A+", color = AmberAccent, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("Qari Pilihan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AmberAccent)
        Spacer(Modifier.height(8.dp))
        var expanded by rememberSaveable { mutableStateOf(false) }
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(Reciters.names[state.preferences.qori] ?: "Pilih Qari", color = TextPrimary)
                Spacer(Modifier.width(6.dp))
                Icon(Icons.Default.ArrowDropDown, null, tint = TextSecondary)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                Reciters.names.forEach { (id, name) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            viewModel.setQori(id)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("Zona Waktu Jadwal Shalat", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AmberAccent)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = zone,
            onValueChange = { zone = it },
            singleLine = true,
            label = { Text("Zona Waktu IANA (misal: Asia/Jakarta, Asia/Makassar)") },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))
        Button(
            onClick = { viewModel.setZone(zone) },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AmberAccent, contentColor = Color.Black)
        ) {
            Text("Simpan Zona Waktu")
        }
    }
}

@Composable
private fun SettingSwitch(label: String, checked: Boolean, onClick: () -> Unit) {
    GlassCard(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
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
}
