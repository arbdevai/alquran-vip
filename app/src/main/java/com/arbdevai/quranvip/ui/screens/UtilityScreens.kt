package com.arbdevai.quranvip.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbdevai.quranvip.data.model.City
import com.arbdevai.quranvip.data.model.Reciters
import com.arbdevai.quranvip.data.model.TasbihHistory
import com.arbdevai.quranvip.ui.AppScreen
import com.arbdevai.quranvip.ui.AppViewModel
import com.arbdevai.quranvip.ui.UiState
import com.arbdevai.quranvip.ui.components.GlassCard
import com.arbdevai.quranvip.ui.theme.*
import com.arbdevai.quranvip.util.CalendarHelper
import java.time.LocalDate

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
            .padding(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 100.dp)
    ) {
        // Clean Header (Title + Location actions)
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Jadwal Salat",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                val cityText = state.preferences.city?.let { "${it.lokasi} (${state.preferences.zone})" } ?: "Lokasi GPS"
                Text(
                    text = cityText,
                    style = MaterialTheme.typography.labelSmall,
                    color = AmberAccent
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilledIconButton(
                    onClick = { cityDialog = true },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = SurfaceCard,
                        contentColor = TextPrimary
                    ),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Default.LocationCity, contentDescription = "Pilih Kota")
                }
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
                        containerColor = SurfaceCard,
                        contentColor = AmberAccent
                    ),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Default.MyLocation, contentDescription = "GPS Otomatis")
                }
            }
        }

        if (state.locationLoading) {
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(color = AmberAccent, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
                Text("Mendeteksi lokasi GPS...", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
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

        // Full Today Prayer Times Table
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
                    Column(Modifier.padding(20.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Waktu Salat Hari Ini",
                                color = TextPrimary,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                day.tanggal,
                                color = AmberAccent,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                        day.times().forEach { (name, time) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    name,
                                    color = if (name in listOf("Subuh", "Zuhur", "Asar", "Magrib", "Isya")) TextPrimary else TextSecondary,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = if (name in listOf("Subuh", "Zuhur", "Asar", "Magrib", "Isya")) FontWeight.Bold else FontWeight.Normal
                                )
                                Text(
                                    time,
                                    color = AmberAccent,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
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
                            "Pilih kota atau aktifkan GPS untuk melihat waktu salat"
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
            .padding(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 100.dp)
    ) {
        // Clean Header (Title + Hijri Info)
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Kalender 3-in-1",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Nasional · Hijriyah (Arab) · Pasaran Jawa",
                    style = MaterialTheme.typography.labelSmall,
                    color = AmberAccent
                )
            }
        }

        Spacer(Modifier.height(16.dp))

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

        Spacer(Modifier.height(10.dp))

        // 3-in-1 Interactive Calendar Grid (Gregorian, Hijri Arabic Numeral, Javanese Pasaran)
        CalendarGrid(state, viewModel)

        Spacer(Modifier.height(18.dp))

        // Selected Date Triple Info Card
        val selectedDate = state.selectedPrayerDate
        val selectedTriple = remember(selectedDate) { CalendarHelper.getDayTriple(selectedDate) }
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(Modifier.padding(18.dp)) {
                Text(
                    text = "Detail Penanggalan",
                    color = AmberAccent,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Tanggal Masehi", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    Text("${selectedDate.dayOfMonth} $monthName ${selectedDate.year}", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                }
                HorizontalDivider(color = BorderSubtle, thickness = 0.5.dp)

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Tanggal Hijriyah", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "${selectedTriple.hijriDayNumber} ${selectedTriple.hijriMonthName} ${selectedTriple.hijriYear} H (${selectedTriple.hijriDayArabic})",
                        color = AmberAccent,
                        fontWeight = FontWeight.Bold
                    )
                }
                HorizontalDivider(color = BorderSubtle, thickness = 0.5.dp)

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Pasaran Jawa", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    Text(selectedTriple.pasaran, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun CalendarGrid(state: UiState, viewModel: AppViewModel) {
    val month = state.prayerMonth
    val first = month.atDay(1)
    val lead = (first.dayOfWeek.value % 7) // 0 for Sunday

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(Modifier.padding(10.dp)) {
            // Day of Week Header
            Row(Modifier.fillMaxWidth()) {
                listOf("Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab").forEach { dayLabel ->
                    Text(
                        text = dayLabel,
                        color = when (dayLabel) {
                            "Jum" -> AmberAccent
                            "Min" -> Color(0xFFFF6B6B)
                            else -> TextSecondary
                        },
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f).padding(vertical = 4.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Days cells
            val cells = List(lead) { null } + (1..month.lengthOfMonth()).map { month.atDay(it) }
            cells.chunked(7).forEach { week ->
                Row(Modifier.fillMaxWidth()) {
                    week.forEach { date ->
                        if (date != null) {
                            val triple = remember(date) { CalendarHelper.getDayTriple(date) }
                            val isSelected = date == state.selectedPrayerDate
                            val isToday = date == LocalDate.now()

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(2.dp)
                                    .height(58.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        when {
                                            isSelected -> AmberAccent.copy(alpha = 0.22f)
                                            isToday -> Color.White.copy(alpha = 0.10f)
                                            else -> SurfaceCard
                                        }
                                    )
                                    .border(
                                        width = if (isSelected) 1.2.dp else 0.5.dp,
                                        color = if (isSelected) AmberAccent else if (isToday) Color.White.copy(alpha = 0.4f) else BorderSubtle,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { viewModel.setPrayerDate(date) }
                                    .padding(horizontal = 4.dp, vertical = 3.dp)
                            ) {
                                // Top-Left: Masehi Number
                                Text(
                                    text = "${triple.gregorianDay}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) AmberAccent else TextPrimary,
                                    modifier = Modifier.align(Alignment.TopStart)
                                )

                                // Top-Right: Pasaran Jawa (Legi, Pahing, etc.)
                                Text(
                                    text = triple.pasaran,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isSelected) AmberAccent else TextSecondary,
                                    modifier = Modifier.align(Alignment.TopEnd)
                                )

                                // Center: Hijri Date in Arabic Numerals (١, ٢, ...)
                                Text(
                                    text = triple.hijriDayArabic,
                                    fontFamily = ArabicFontFamily,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) AmberAccent else Color(0xFFF8FAFC),
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .offset(y = 5.dp)
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f).padding(2.dp))
                        }
                    }
                    repeat(7 - week.size) {
                        Spacer(Modifier.weight(1f).padding(2.dp))
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
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Ayat Disimpan",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(vertical = 4.dp)
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
    val haptic = LocalHapticFeedback.current
    val zikirPresets = listOf(
        "Subhanallah",
        "Alhamdulillah",
        "Allahu Akbar",
        "Astaghfirullah",
        "La ilaha illallah"
    )

    val currentTarget = state.preferences.tasbihTarget
    val currentCount = state.preferences.tasbih
    val selectedZikir = state.tasbihZikir

    var isPocketLockActive by rememberSaveable { mutableStateOf(false) }

    // Pocket Lock Fullscreen Overlay (Prevents accidental screen touches, Volume key still counts)
    if (isPocketLockActive) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            isPocketLockActive = false
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Terkunci",
                    tint = AmberAccent,
                    modifier = Modifier.size(54.dp)
                )
                Spacer(Modifier.height(18.dp))
                Text(
                    text = "$currentCount",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = selectedZikir,
                    style = MaterialTheme.typography.titleMedium,
                    color = AmberAccent
                )
                Spacer(Modifier.height(28.dp))
                Text(
                    text = "Mode Saku Aktif",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Gunakan tombol volume (+ / -) untuk berzikir\nTahan layar 1 detik untuk buka kunci",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgCanvas)
            .verticalScroll(rememberScrollState())
            .padding(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Screen Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Tasbih Digital",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Tombol Volume HP (+/-) Aktif",
                    style = MaterialTheme.typography.labelSmall,
                    color = AmberAccent
                )
            }
            FilledTonalButton(
                onClick = { isPocketLockActive = true },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = SurfaceCard,
                    contentColor = TextPrimary
                )
            ) {
                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Kunci Layar", fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(14.dp))

        // Preset Zikir Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            zikirPresets.take(3).forEach { zikir ->
                val isSelected = selectedZikir == zikir
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.setTasbihZikir(zikir) },
                    label = { Text(zikir, fontSize = 11.5.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AmberAccent,
                        selectedLabelColor = Color.Black
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // Target Selector Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Target:", color = TextSecondary, fontSize = 12.sp, modifier = Modifier.padding(end = 4.dp))
            listOf(33, 99, 1000, 0).forEach { targetValue ->
                val isSelected = currentTarget == targetValue
                val label = if (targetValue == 0) "Bebas" else "$targetValue"
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) AmberAccent else SurfaceCard,
                    modifier = Modifier
                        .clickable { viewModel.setTasbihTarget(targetValue) }
                        .padding(vertical = 2.dp)
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) Color.Black else TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        // Speedometer Arc Gauge with Big Tappable Area
        Box(
            modifier = Modifier
                .size(260.dp)
                .clickable {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    val reached = viewModel.incrementTasbih(selectedZikir)
                    if (reached) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            // Speedometer Arc Canvas
            val progressFraction = if (currentTarget > 0) {
                (currentCount.toFloat() / currentTarget).coerceIn(0f, 1f)
            } else 1f
            val animatedSweep by animateFloatAsState(
                targetValue = progressFraction * 240f,
                animationSpec = tween(150),
                label = "gaugeSweep"
            )

            Canvas(modifier = Modifier.fillMaxSize().padding(14.dp)) {
                val strokeWidth = 16.dp.toPx()
                val diameter = size.minDimension - strokeWidth
                val topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)
                val arcSize = Size(diameter, diameter)

                // Background track arc (240 degrees from 150° to 390°)
                drawArc(
                    color = Color(0x24FFFFFF),
                    startAngle = 150f,
                    sweepAngle = 240f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Active progress arc
                if (animatedSweep > 0f) {
                    drawArc(
                        color = AmberAccent,
                        startAngle = 150f,
                        sweepAngle = animatedSweep,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
            }

            // Central Counter and Zikir Information
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$currentCount",
                    fontSize = 58.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberAccent
                )
                Text(
                    text = if (currentTarget > 0) "Target: $currentTarget" else "Target: Bebas",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = selectedZikir,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        // Reset Counter Button
        OutlinedButton(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                viewModel.resetTasbih()
            },
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, tint = AmberAccent)
            Spacer(Modifier.width(6.dp))
            Text("Reset Hitungan", color = AmberAccent)
        }

        Spacer(Modifier.height(26.dp))

        // Zikir History Section
        val historyList = state.preferences.tasbihHistory
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Riwayat Zikir",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    if (historyList.isNotEmpty()) {
                        TextButton(onClick = viewModel::clearTasbihHistory) {
                            Text("Hapus Riwayat", color = Color(0xFFFF6B6B), fontSize = 12.sp)
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                if (historyList.isEmpty()) {
                    Text(
                        text = "Belum ada riwayat zikir terselesaikan. Setiap target tercapai, riwayat akan otomatis tersimpan di sini.",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        historyList.forEach { history ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = history.zikir,
                                        color = TextPrimary,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = history.date,
                                        color = TextSecondary,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = AmberAccent.copy(alpha = 0.16f)
                                ) {
                                    Text(
                                        text = "${history.count}x Selesai",
                                        color = AmberAccent,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            HorizontalDivider(color = BorderSubtle, thickness = 0.5.dp)
                        }
                    }
                }
            }
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
            .padding(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 100.dp)
    ) {
        Text(
            text = "Pengaturan",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(vertical = 4.dp)
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
