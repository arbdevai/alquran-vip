package com.arbdevai.quranvip.data.repository

import com.arbdevai.quranvip.data.local.ResponseCache
import com.arbdevai.quranvip.data.model.*
import com.arbdevai.quranvip.data.remote.MuslimApi
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

class PrayerRepository(private val api: MuslimApi, private val cache: ResponseCache) {
    suspend fun getCities(): List<City> = cache.get("cities_v3", 30L * 86400000) {
        api.cities().requireData()
    }

    suspend fun searchCities(keyword: String): List<City> {
        val term = keyword.trim()
        if (term.isBlank()) return emptyList()
        return getCities().filter { it.lokasi.contains(term, ignoreCase = true) }
    }

    suspend fun getPrayerToday(cityId: String, timeZone: String): PrayerData =
        getPrayerMonth(cityId, YearMonth.now(ZoneId.of(timeZone)), timeZone)

    suspend fun getPrayerMonth(cityId: String, yearMonth: YearMonth, timeZone: String): PrayerData =
        cache.get("prayer_${cityId}_${yearMonth}_$timeZone", 86400000L) {
            api.prayerPeriod(cityId, yearMonth.year.toString(), "%02d".format(yearMonth.monthValue), timeZone).requireData().also { data ->
                require(data.id == cityId && data.jadwal.keys.any { it.startsWith(yearMonth.toString()) }) {
                    "Respons jadwal tidak sesuai kota atau bulan yang diminta"
                }
            }
        }

    suspend fun getCalendar(date: LocalDate, timeZone: String): CalendarData =
        cache.get("calendar_${date}_$timeZone", 86400000L) {
            api.calendar(date.toString(), timeZone).requireData().also { data ->
                require(LocalDate.of(data.ce.year, data.ce.month, data.ce.day) == date) {
                    "Respons kalender tidak sesuai tanggal yang diminta"
                }
            }
        }

    suspend fun getCalendarToday(timeZone: String): CalendarData = getCalendar(LocalDate.now(ZoneId.of(timeZone)), timeZone)
}
