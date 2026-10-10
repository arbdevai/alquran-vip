package com.arbdevai.quranvip.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.arbdevai.quranvip.QuranVipApplication
import com.arbdevai.quranvip.audio.PlaybackController
import com.arbdevai.quranvip.audio.PlaybackState
import com.arbdevai.quranvip.data.model.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

private const val DEFAULT_ZONE = "Asia/Jakarta"
enum class AppScreen { HOME, QURAN, PRAYER, CALENDAR, BOOKMARKS, SETTINGS, TASBIH }

data class UiState(
    val screen: AppScreen = AppScreen.HOME,
    val preferences: UserPreferences = UserPreferences(),
    val preferencesReady: Boolean = false,
    val message: String? = null,
    val surahs: List<Surah> = emptyList(),
    val surahQuery: String = "",
    val quranLoading: Boolean = false,
    val quranError: String? = null,
    val readerNumber: Int? = null,
    val readerStartAyah: Int = 1,
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
    val citiesError: String? = null,
    val prayerMonth: YearMonth = YearMonth.now(ZoneId.of(DEFAULT_ZONE)),
    val prayerData: PrayerData? = null,
    val prayerLoading: Boolean = false,
    val prayerError: String? = null,
    val selectedPrayerDate: LocalDate = LocalDate.now(ZoneId.of(DEFAULT_ZONE)),
    val selectedCalendar: CalendarData? = null,
    val calendarLoading: Boolean = false,
    val calendarError: String? = null,
    val locationLoading: Boolean = false,
    val locationError: String? = null,
    val playback: PlaybackState = PlaybackState(),
    val tasbihZikir: String = "Subhanallah",
    val latestUpdate: GithubRelease? = null
) {
    val filteredSurahs: List<Surah>
        get() = surahs.filter {
            val query = surahQuery.trim()
            query.isEmpty() || it.namaLatin.contains(query, true) || it.nama.contains(query) ||
                it.arti.contains(query, true) || it.nomor.toString() == query
        }
    val selectedPrayerDay: PrayerDay?
        get() = prayerData?.jadwal?.get(selectedPrayerDate.toString())
    val todayPrayerDay: PrayerDay?
        get() = prayerData?.jadwal?.get(LocalDate.now(runCatching { ZoneId.of(preferences.zone) }.getOrDefault(ZoneId.of(DEFAULT_ZONE))).toString()) ?: selectedPrayerDay
}

class AppViewModel(application: Application, private val savedState: SavedStateHandle) : AndroidViewModel(application) {
    private val app = application as QuranVipApplication
    private val playbackController = PlaybackController(application.applicationContext)
    private val _state = MutableStateFlow(UiState(screen = runCatching {
        AppScreen.valueOf(savedState.get<String>("screen") ?: "HOME")
    }.getOrDefault(AppScreen.HOME)))
    val state: StateFlow<UiState> = _state.asStateFlow()
    private var readerRequest = 0
    private var tafsirRequest = 0
    private var locationRequest = 0
    private var calendarRequest = 0
    private var prayerRequest = 0
    private var citySearchRequest = 0
    private var readerJob: Job? = null
    private var tafsirJob: Job? = null
    private var locationJob: Job? = null
    private var calendarJob: Job? = null
    private var prayerJob: Job? = null
    private var citySearchJob: Job? = null
    private var lastForegroundDate: LocalDate? = null

    init {
        viewModelScope.launch {
            app.preferences.stream.catch { error ->
                _state.update { it.copy(message = "Gagal membaca pengaturan: ${error.message}") }
            }.collect { prefs ->
                val previous = _state.value
                val changed = !previous.preferencesReady || previous.preferences.city != prefs.city || previous.preferences.zone != prefs.zone
                _state.update { it.copy(preferences = prefs, preferencesReady = true) }
                if (changed) refreshPrayerForTodayIfNeeded(force = true)
            }
        }
        viewModelScope.launch {
            playbackController.state.collect { playback -> _state.update { it.copy(playback = playback) } }
        }
        loadSurahs()
        savedState.get<Int>("reader")?.let { openSurah(it, savedState.get<Int>("ayah") ?: 1) }
        checkForUpdate()
    }
    
    private fun checkForUpdate() {
        viewModelScope.launch {
            try {
                val release = com.arbdevai.quranvip.data.remote.NetworkModule.github.getLatestRelease()
                val currentVersion = "v${com.arbdevai.quranvip.BuildConfig.VERSION_NAME}"
                if (release.tag_name > currentVersion || (release.tag_name != currentVersion && !release.tag_name.contains("alpha"))) {
                    _state.update { it.copy(latestUpdate = release) }
                }
            } catch (e: Exception) {
                // Ignore update check failure
            }
        }
    }

    // All requests preserve structured cancellation instead of swallowing cancellation as an error.
    private suspend fun <T> request(block: suspend () -> T): Result<T> = try {
        val value = block()
        currentCoroutineContext().ensureActive()
        Result.success(value)
    } catch (error: CancellationException) { throw error }
      catch (error: Exception) { Result.failure(error) }

    private fun persist(block: suspend () -> Unit) = viewModelScope.launch {
        request(block).onFailure { _state.update { state -> state.copy(message = "Perubahan belum tersimpan. Coba lagi. ${it.message.orEmpty()}") } }
    }

    fun clearMessage() = _state.update { it.copy(message = null) }
    fun setScreen(screen: AppScreen) {
        savedState["screen"] = screen.name
        _state.update { it.copy(screen = screen) }
        if (screen == AppScreen.PRAYER || screen == AppScreen.CALENDAR) refreshPrayerForTodayIfNeeded()
    }
    fun setSurahQuery(query: String) = _state.update { it.copy(surahQuery = query) }
    fun loadSurahs() {
        if (_state.value.quranLoading) return
        viewModelScope.launch {
            _state.update { it.copy(quranLoading = true, quranError = null) }
            request { app.quranRepository.getSurahs() }
                .onSuccess { surahs -> _state.update { it.copy(surahs = surahs, quranLoading = false) } }
                .onFailure { _state.update { state -> state.copy(quranLoading = false, quranError = "Gagal memuat surah. Periksa koneksi internet.") } }
        }
    }

    fun openSurah(number: Int, ayah: Int = 1) {
        if (number !in 1..114) return
        val token = ++readerRequest
        readerJob?.cancel()
        dismissTafsir()
        savedState["reader"] = number
        savedState["ayah"] = ayah
        setScreen(AppScreen.QURAN)
        _state.update { it.copy(readerNumber = number, readerStartAyah = ayah, reader = null, readerLoading = true, readerError = null) }
        readerJob = viewModelScope.launch {
            request { app.quranRepository.getSurah(number) }
                .onSuccess { surah -> if (token == readerRequest) {
                    val start = ayah.coerceIn(1, surah.jumlahAyat.coerceAtLeast(1))
                    _state.update { it.copy(reader = surah, readerStartAyah = start, readerLoading = false) }
                } }
                .onFailure { if (token == readerRequest) _state.update { state -> state.copy(readerLoading = false, readerError = "Surah belum dapat dibuka. Periksa koneksi lalu coba lagi.") } }
        }
    }
    fun retryReader() { _state.value.readerNumber?.let { openSurah(it, _state.value.readerStartAyah) } }
    fun closeReader() {
        readerRequest++
        readerJob?.cancel()
        dismissTafsir()
        savedState.remove<Int>("reader")
        savedState.remove<Int>("ayah")
        _state.update { it.copy(readerNumber = null, reader = null, readerLoading = false, readerError = null) }
    }
    fun recordReading(ayah: Int) {
        val reader = _state.value.reader ?: return
        if (reader.ayat.none { it.nomorAyat == ayah }) return
        savedState["ayah"] = ayah
        val position = ReadingPosition(reader.nomor, ayah, reader.namaLatin)
        if (_state.value.preferences.lastRead != position) persist { app.preferences.setLastRead(position) }
    }
    fun playSurah(startAyah: Int = 1, singleAyahOnly: Boolean = false) {
        _state.value.reader?.let { playbackController.play(it, _state.value.preferences.qori, startAyah, singleAyahOnly) }
    }
    fun togglePlayback() = playbackController.toggle()
    fun stopPlayback() = playbackController.stop()
    fun playAyah(ayah: Int) {
        val playback = _state.value.playback
        if (playback.surah == _state.value.reader?.nomor && playback.ayah == ayah && playback.error == null) togglePlayback()
        else playSurah(ayah, true)
    }
    fun toggleBookmark(ayah: Ayah) {
        val reader = _state.value.reader ?: return
        removeOrAddBookmark(Bookmark(reader.nomor, ayah.nomorAyat, reader.namaLatin, ayah.teksArab, ayah.teksIndonesia))
    }
    fun removeOrAddBookmark(bookmark: Bookmark) = persist { app.preferences.toggleBookmark(bookmark) }
    fun loadTafsir(ayah: Int) {
        val reader = _state.value.reader ?: return
        val token = ++tafsirRequest
        tafsirJob?.cancel()
        _state.update { it.copy(tafsirAyah = ayah, tafsir = null, tafsirLoading = true, tafsirError = null) }
        tafsirJob = viewModelScope.launch {
            request { app.quranRepository.getTafsir(reader.nomor) }
                .onSuccess { tafsir -> if (token == tafsirRequest) _state.update { it.copy(tafsir = tafsir, tafsirLoading = false) } }
                .onFailure { if (token == tafsirRequest) _state.update { state -> state.copy(tafsirLoading = false, tafsirError = "Tafsir belum tersedia. Coba lagi.") } }
        }
    }
    fun dismissTafsir() {
        tafsirRequest++
        tafsirJob?.cancel()
        _state.update { it.copy(tafsir = null, tafsirAyah = null, tafsirLoading = false, tafsirError = null) }
    }
    fun setQori(id: String) = persist { app.preferences.setQori(id) }
    fun setFontSize(size: Int) = persist { app.preferences.setFontSize(size) }
    fun toggleLatin() = persist { app.preferences.toggleLatin() }
    fun toggleTranslation() = persist { app.preferences.toggleTranslation() }

    fun setCityQuery(query: String) {
        val token = ++citySearchRequest
        citySearchJob?.cancel()
        _state.update { it.copy(cityQuery = query, citiesLoading = true, citiesError = null, cityResults = emptyList()) }
        citySearchJob = viewModelScope.launch {
            delay(300)
            request { app.prayerRepository.searchCities(query) }
                .onSuccess { cities -> if (token == citySearchRequest) _state.update { it.copy(cityResults = cities, citiesLoading = false) } }
                .onFailure { if (token == citySearchRequest) _state.update { state -> state.copy(citiesLoading = false, citiesError = "Daftar kota gagal dimuat. Coba lagi.") } }
        }
    }
    fun cancelLocation() {
        locationRequest++
        locationJob?.cancel()
        _state.update { it.copy(locationLoading = false) }
    }
    fun chooseCity(city: City, zone: String) {
        cancelLocation()
        if (!validZone(zone)) return
        persist { app.preferences.setCity(city, zone.trim()) }
    }
    fun setZone(zone: String) {
        cancelLocation()
        if (validZone(zone)) persist { app.preferences.setZone(zone.trim()) }
    }
    private fun validZone(zone: String): Boolean {
        val valid = runCatching { ZoneId.of(zone.trim()) }.isSuccess
        if (!valid) _state.update { it.copy(message = "Zona waktu tidak valid. Gunakan Asia/Jakarta, Asia/Makassar, atau Asia/Jayapura.") }
        return valid
    }
    fun detectLocation() {
        cancelLocation()
        val token = ++locationRequest
        _state.update { it.copy(locationLoading = true, locationError = null) }
        locationJob = viewModelScope.launch {
            request { withTimeout(25_000) { app.locationRepository.detectCurrentCity() } }
                .onSuccess { result -> if (token == locationRequest) {
                    _state.update { it.copy(locationLoading = false, locationError = if (result == null) "Lokasi belum ditemukan. Aktifkan GPS atau pilih kota secara manual." else null) }
                    if (result != null) app.preferences.setCity(result.first, result.second)
                } }
                .onFailure { if (token == locationRequest) _state.update { state -> state.copy(locationLoading = false, locationError = "Deteksi lokasi gagal. Pilih kota secara manual atau coba lagi.") } }
        }
        locationJob?.invokeOnCompletion {
            if (token == locationRequest && _state.value.locationLoading) _state.update { it.copy(locationLoading = false, locationError = "Lokasi belum ditemukan. Coba lagi atau pilih kota manual.") }
        }
    }
    fun locationPermissionDenied() = _state.update { it.copy(locationError = "Izin lokasi ditolak. Anda tetap dapat memilih kota secara manual.") }
    fun setPrayerDate(date: LocalDate) {
        _state.update { it.copy(selectedPrayerDate = date, selectedCalendar = null) }
        loadCalendar(date)
    }
    fun changePrayerMonth(delta: Long) {
        val month = _state.value.prayerMonth.plusMonths(delta)
        _state.update { it.copy(prayerMonth = month, selectedPrayerDate = month.atDay(1)) }
        loadPrayerMonth(month)
        loadCalendar(month.atDay(1))
    }
    fun refreshPrayerForTodayIfNeeded(force: Boolean = false) {
        if (!_state.value.preferencesReady) return
        val today = LocalDate.now(safeZone(_state.value.preferences.zone))
        if (!force && lastForegroundDate == today) return
        lastForegroundDate = today
        _state.update { it.copy(selectedPrayerDate = today, prayerMonth = YearMonth.from(today)) }
        loadPrayerMonth(YearMonth.from(today))
        loadCalendar(today)
    }
    fun onForeground() = refreshPrayerForTodayIfNeeded()
    fun showToday() = refreshPrayerForTodayIfNeeded(force = true)
    fun retryPrayer() = loadPrayerMonth(_state.value.prayerMonth)
    fun retryCalendar() = loadCalendar(_state.value.selectedPrayerDate)
    private fun loadPrayerMonth(month: YearMonth) {
        val token = ++prayerRequest
        prayerJob?.cancel()
        val prefs = _state.value.preferences
        val city = prefs.city
        _state.update { it.copy(prayerData = null, prayerLoading = city != null, prayerError = null) }
        if (city == null) return
        prayerJob = viewModelScope.launch {
            request { app.prayerRepository.getPrayerMonth(city.id, month, prefs.zone) }
                .onSuccess { data -> if (token == prayerRequest) _state.update { it.copy(prayerData = data, prayerLoading = false) } }
                .onFailure { if (token == prayerRequest) _state.update { state -> state.copy(prayerLoading = false, prayerError = "Jadwal shalat gagal dimuat. Periksa koneksi lalu coba lagi.") } }
        }
    }
    private fun loadCalendar(date: LocalDate) {
        val token = ++calendarRequest
        calendarJob?.cancel()
        val zone = _state.value.preferences.zone
        _state.update { it.copy(selectedCalendar = null, calendarLoading = true, calendarError = null) }
        calendarJob = viewModelScope.launch {
            request { app.prayerRepository.getCalendar(date, zone) }
                .onSuccess { data -> if (token == calendarRequest) _state.update { it.copy(selectedCalendar = data, calendarLoading = false) } }
                .onFailure { if (token == calendarRequest) _state.update { state -> state.copy(calendarLoading = false, calendarError = "Kalender Hijriah gagal dimuat.") } }
        }
    }
    fun incrementTasbih(zikirName: String = "Subhanallah"): Boolean {
        val prefs = _state.value.preferences
        val current = prefs.tasbih
        val target = prefs.tasbihTarget
        if (target > 0 && current + 1 >= target) {
            val history = TasbihHistory(
                id = System.currentTimeMillis(),
                zikir = zikirName,
                count = target,
                date = LocalDate.now().toString()
            )
            persist {
                app.preferences.addTasbihHistory(history)
                app.preferences.resetTasbih()
            }
            return true
        } else {
            persist { app.preferences.incrementTasbih() }
            return false
        }
    }
    fun resetTasbih() = persist { app.preferences.resetTasbih() }
    fun setTasbihTarget(target: Int) = persist { app.preferences.setTasbihTarget(target) }
    fun clearTasbihHistory() = persist { app.preferences.clearTasbihHistory() }

    fun setTasbihZikir(name: String) = _state.update { it.copy(tasbihZikir = name) }
    fun handleVolumeKeyAsTasbih(): Boolean {
        if (_state.value.screen == AppScreen.TASBIH) {
            incrementTasbih(_state.value.tasbihZikir)
            return true
        }
        return false
    }

    private fun safeZone(value: String) = runCatching { ZoneId.of(value) }.getOrDefault(ZoneId.of(DEFAULT_ZONE))
    override fun onCleared() { playbackController.close(); super.onCleared() }
}
