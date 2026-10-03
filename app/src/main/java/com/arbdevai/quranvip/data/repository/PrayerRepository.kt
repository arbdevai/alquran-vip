package com.arbdevai.quranvip.data.repository

import com.arbdevai.quranvip.data.model.CalendarData
import com.arbdevai.quranvip.data.model.City
import com.arbdevai.quranvip.data.model.PrayerData
import com.arbdevai.quranvip.data.remote.MuslimApi

class PrayerRepository(private val api: MuslimApi) {
    private var allCities: List<City>? = null

    suspend fun getCities(): List<City> {
        allCities?.let { return it }
        val response = api.cities()
        return response.requireData().also { allCities = it }
    }

    suspend fun searchCities(keyword: String): List<City> {
        val trimmed = keyword.trim()
        if (trimmed.isBlank()) return getCities()
        val response = api.searchCities(trimmed)
        return response.data ?: emptyList()
    }

    suspend fun getPrayerToday(cityId: String, timeZone: String): PrayerData {
        val response = api.prayerToday(cityId, timeZone)
        return response.requireData()
    }

    suspend fun getPrayerMonth(cityId: String, yearMonth: java.time.YearMonth, timeZone: String): PrayerData {
        val period = java.time.format.DateTimeFormatter.ofPattern("uuuu-MM").format(yearMonth)
        val response = api.prayerPeriod(cityId, period, timeZone)
        return response.requireData()
    }

    suspend fun getCalendar(date: java.time.LocalDate, timeZone: String): CalendarData {
        val dateStr = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE.format(date)
        val response = api.calendar(dateStr, timeZone)
        return response.requireData()
    }

    suspend fun getCalendarToday(timeZone: String): CalendarData {
        val response = api.calendarToday(timeZone)
        return response.requireData()
    }
}
