package com.arbdevai.quranvip.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arbdevai.quranvip.QuranVipApplication
import com.arbdevai.quranvip.audio.PlaybackController
import com.arbdevai.quranvip.audio.PlaybackState
import com.arbdevai.quranvip.data.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private const val DEFAULT_ZONE = "Asia/Jakarta"

enum class AppScreen { HOME, QURAN, PRAYER, BOOKMARKS, SETTINGS, TASBIH }

data class UiState(
    val screen: AppScreen = AppScreen.HOME,
    val preferences: Preferences = Preferences(),
    val surahs: List<Surah> = emptyList(),
    val surahQuery: String = "",
    val quranLoading: Boolean = false,
    val quranError: String? = null,
    val reader: Surah? = null,
    val readerLoading: Boolean = false,
    val readerError: String? = null,
    val tafsir: TafsirSurah? = null,
    val tafsirAyah: Int? = null,
    val tafsirLoading: Boolean = false,
    val tafsirError: String? = null,
    val cityQuery: String = "",
    val cityResults: List<City> = emptyList(),
    val citiesLoading: Boolean = false,
    val prayerMonth: YearMonth = YearMonth.now(),
    val prayerData: PrayerData? = null,
    val prayerLoading: Boolean = false,
    val prayerError: String? = null,
    val selectedPrayerDate: LocalDate = LocalDate.now(),
    val selectedCalendar: CalendarData? = null,
    val calendarLoading: Boolean = false,
    val calendarError: String? = null,
    val locationLoading: Boolean = false,
    val locationError: String? = null,
    val tasbih: Int = 0,
    val playback: PlaybackState = PlaybackState()
) {
    val filteredSurahs: List<Surah>
        get() = if (surahQuery.isBlank()) surahs else surahs.filter {
            it.namaLatin.contains(surahQuery, true) || it.nama.contains(surahQuery, true) ||
                it.nomor.toString() == surahQuery.trim()
        }
    val selectedPrayerDay: PrayerDay?
        get() = prayerData?.jadwal?.get(selectedPrayerDate.toString())
}

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as QuranVipApplication
    private val playbackController = PlaybackController(application.applicationContext)
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private var readerRequest = 0
    private var tafsirRequest = 0
    private var locationRequest = 0
    private var calendarRequest = 0
    private var prayerRequest = 0
    private var citySearchJob: Job? = null
    private var lastForegroundDate: LocalDate? = null

    init {
        viewModelScope.launch {
            app.preferences.stream
                .catch { error -> _state.update { it.copy(quranError = error.message ?: "Gagal membaca pengaturan") } }
                .collect { prefs ->
                    _state.update { it.copy(preferences = prefs, tasbih = prefs.tasbih) }
                }
        }
        viewModelScope.launch {
            playbackController.state.collect { playback -> _state.update { it.copy(playback = playback) } }
        }
        loadSurahs()
    }

    fun setScreen(screen: AppScreen) {
        _state.update { it.copy(screen = screen) }
        if (screen == AppScreen.PRAYER) refreshPrayerForTodayIfNeeded(force = false)
    }

    fun setSurahQuery(query: String) = _state.update { it.copy(surahQuery = query) }

    fun loadSurahs() {
        if (_state.value.quranLoading) return
        viewModelScope.launch {
            _state.update { it.copy(quranLoading = true, quranError = null) }
            runCatching { app.quranRepository.getSurahs() }
                .onSuccess { surahs -> _state.update { it.copy(surahs = surahs, quranLoading = false) } }
                .onFailure { error -> _state.update { it.copy(quranLoading = false, quranError = error.message ?: "Gagal memuat daftar surah") } }
        }
    }

    fun openSurah(number: Int, ayah: Int = 1) {
        val token = ++readerRequest
        viewModelScope.launch {
            _state.update { it.copy(screen = AppScreen.QURAN, reader = null, readerLoading = true, readerError = null, tafsir = null) }
            runCatching { app.quranRepository.getSurah(number) }
                .onSuccess { surah ->
                    if (token != readerRequest) return@onSuccess
                    _state.update { it.copy(reader = surah, readerLoading = false) }
                    app.preferences.setLastRead(ReadingPosition(surah.nomor, ayah.coerceIn(1, surah.jumlahAyat), surah.namaLatin))
                }
                .onFailure { error ->
                    if (token == readerRequest) _state.update { it.copy(readerLoading = false, readerError = error.message ?: "Gagal membuka surah") }
                }
        }
    }

    fun closeReader() {
        readerRequest++
        tafsirRequest++
        _state.update { it.copy(reader = null, tafsir = null, tafsirAyah = null, readerError = null) }
    }

    fun playSurah(startAyah: Int = 1, singleAyahOnly: Boolean = false) {
        _state.value.reader?.let { playbackController.play(it, _state.value.preferences.qori, startAyah, singleAyahOnly) }
    }

    fun togglePlayback() = playbackController.toggle()
    fun stopPlayback() = playbackController.stop()

    fun playAyah(ayah: Int) {
        val playback = _state.value.playback
        if (playback.playing && playback.surah == _state.value.reader?.nomor && playback.ayah == ayah) {
            playbackController.toggle()
        } else {
            playSurah(ayah, true)
        }
    }

    fun toggleBookmark(ayah: Ayah) {
        val reader = _state.value.reader ?: return
        viewModelScope.launch {
            app.preferences.toggleBookmark(Bookmark(reader.nomor, ayah.nomorAyat, reader.namaLatin, ayah.teksArab, ayah.teksIndonesia))
        }
    }

    fun isBookmarked(surah: Int, ayah: Int) = _state.value.preferences.bookmarks.any { it.surah == surah && it.ayah == ayah }

    fun loadTafsir(ayah: Int) {
        val reader = _state.value.reader ?: return
        val token = ++tafsirRequest
        viewModelScope.launch {
            _state.update { it.copy(tafsirAyah = ayah, tafsirLoading = true, tafsirError = null) }
            runCatching { app.quranRepository.getTafsir(reader.nomor) }
                .onSuccess { tafsir -> if (token == tafsirRequest) _state.update { it.copy(tafsir = tafsir, tafsirLoading = false) } }
                .onFailure { error -> if (token == tafsirRequest) _state.update { it.copy(tafsirLoading = false, tafsirError = error.message ?: "Gagal memuat tafsir") } }
        }
    }

    fun dismissTafsir() {
        tafsirRequest++
        _state.update { it.copy(tafsir = null, tafsirAyah = null, tafsirError = null) }
    }

    fun setQori(id: String) = viewModelScope.launch { app.preferences.setQori(id) }
    fun setFontSize(size: Int) = viewModelScope.launch { app.preferences.setFontSize(size) }
    fun toggleLatin() = viewModelScope.launch { app.preferences.toggleLatin() }
    fun toggleTranslation() = viewModelScope.launch { app.preferences.toggleTranslation() }

    fun setCityQuery(query: String) {
        _state.update { it.copy(cityQuery = query) }
        citySearchJob?.cancel()
        citySearchJob = viewModelScope.launch {
            _state.update { it.copy(citiesLoading = true) }
            val results = runCatching { app.prayerRepository.searchCities(query) }.getOrDefault(emptyList())
            _state.update { it.copy(cityResults = results, citiesLoading = false) }
        }
    }

    fun clearCityResults() = _state.update { it.copy(cityResults = emptyList(), cityQuery = "") }

    fun chooseCity(city: City, zone: String = _state.value.preferences.zone) {
        val cleanZone = zone.trim().ifBlank { DEFAULT_ZONE }
        viewModelScope.launch {
            app.preferences.setCity(city, cleanZone)
            _state.update { it.copy(cityResults = emptyList(), cityQuery = "", locationError = null) }
            refreshPrayerForTodayIfNeeded(force = true)
        }
    }

    fun setZone(zone: String) = viewModelScope.launch { app.preferences.setZone(zone.trim()) }

    fun detectLocation() {
        val token = ++locationRequest
        viewModelScope.launch {
            _state.update { it.copy(locationLoading = true, locationError = null) }
            runCatching { app.locationRepository.detectCurrentCity() }
                .onSuccess { result ->
                    if (token != locationRequest) return@onSuccess
                    if (result == null) {
                        _state.update { it.copy(locationLoading = false, locationError = "Lokasi tidak ditemukan. Pilih kota secara manual.") }
                    } else {
                        val (city, zone) = result
                        app.preferences.setCity(city, zone)
                        _state.update { it.copy(locationLoading = false, locationError = null) }
                        refreshPrayerForTodayIfNeeded(force = true)
                    }
                }
                .onFailure { error -> if (token == locationRequest) _state.update { it.copy(locationLoading = false, locationError = error.message ?: "Gagal mendapatkan lokasi") } }
        }
    }

    fun locationPermissionDenied() = _state.update { it.copy(locationError = "Izin lokasi diperlukan untuk mendeteksi kota. Pilih kota secara manual.") }

    fun setPrayerDate(date: LocalDate) {
        val zoneDate = date
        _state.update { it.copy(selectedPrayerDate = zoneDate) }
        loadCalendar(zoneDate)
    }

    fun changePrayerMonth(delta: Long) {
        val month = _state.value.prayerMonth.plusMonths(delta)
        val date = month.atDay(1)
        _state.update { it.copy(prayerMonth = month, selectedPrayerDate = date, prayerData = null, prayerError = null) }
        loadPrayerMonth(month)
        loadCalendar(date)
    }

    fun refreshPrayerForTodayIfNeeded(force: Boolean = false) {
        val prefs = _state.value.preferences
        val zone = safeZone(prefs.zone)
        val today = LocalDate.now(zone)
        if (!force && lastForegroundDate == today && _state.value.prayerData != null) return
        lastForegroundDate = today
        _state.update { it.copy(selectedPrayerDate = today, prayerMonth = YearMonth.from(today)) }
        if (prefs.city == null) {
            _state.update { it.copy(prayerData = null, prayerError = null, selectedCalendar = null) }
            return
        }
        loadPrayerMonth(YearMonth.from(today))
        loadCalendar(today)
    }

    fun onForeground() = refreshPrayerForTodayIfNeeded(force = false)

    fun retryPrayer() = refreshPrayerForTodayIfNeeded(force = true)
    fun retryCalendar() = loadCalendar(_state.value.selectedPrayerDate)

    private fun loadPrayerMonth(month: YearMonth) {
        val city = _state.value.preferences.city ?: return
        val token = ++prayerRequest
        viewModelScope.launch {
            _state.update { it.copy(prayerLoading = true, prayerError = null) }
            runCatching { app.prayerRepository.getPrayerMonth(city.id, month, _state.value.preferences.zone.ifBlank { DEFAULT_ZONE }) }
                .onSuccess { data -> if (token == prayerRequest) _state.update { it.copy(prayerData = data, prayerLoading = false, prayerMonth = month) } }
                .onFailure { error -> if (token == prayerRequest) _state.update { it.copy(prayerLoading = false, prayerError = error.message ?: "Gagal memuat jadwal shalat") } }
        }
    }

    private fun loadCalendar(date: LocalDate) {
        val token = ++calendarRequest
        val zone = _state.value.preferences.zone.ifBlank { DEFAULT_ZONE }
        viewModelScope.launch {
            _state.update { it.copy(calendarLoading = true, calendarError = null) }
            runCatching { app.prayerRepository.getCalendar(date, zone) }
                .onSuccess { data -> if (token == calendarRequest) _state.update { it.copy(selectedCalendar = data, calendarLoading = false) } }
                .onFailure { error -> if (token == calendarRequest) _state.update { it.copy(calendarLoading = false, calendarError = error.message ?: "Gagal memuat kalender Hijriah") } }
        }
    }

    fun incrementTasbih() = viewModelScope.launch { app.preferences.incrementTasbih() }
    fun resetTasbih() = viewModelScope.launch { app.preferences.resetTasbih() }

    private fun safeZone(value: String): ZoneId = runCatching { ZoneId.of(value) }.getOrDefault(ZoneId.of(DEFAULT_ZONE))

    override fun onCleared() {
        playbackController.close()
        super.onCleared()
    }
}
