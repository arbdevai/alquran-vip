package com.arbdevai.quranvip.data.remote

import com.arbdevai.quranvip.data.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EQuranApi {
    @GET("api/v2/surat")
    suspend fun surahs(): QuranResponse<List<Surah>>

    @GET("api/v2/surat/{number}")
    suspend fun surah(@Path("number") number: Int): QuranResponse<Surah>

    @GET("api/v2/tafsir/{number}")
    suspend fun tafsir(@Path("number") number: Int): QuranResponse<TafsirSurah>
}

interface MuslimApi {
    @GET("sholat/kabkota/semua")
    suspend fun cities(): MuslimResponse<List<City>>

    @GET("sholat/kabkota/cari/{keyword}")
    suspend fun searchCities(@Path("keyword") keyword: String): MuslimResponse<List<City>>

    @GET("sholat/jadwal/{id}/today")
    suspend fun prayerToday(@Path("id") id: String, @Query("tz") timeZone: String): MuslimResponse<PrayerData>

    @GET("sholat/jadwal/{id}/{period}")
    suspend fun prayerPeriod(@Path("id") id: String, @Path("period") period: String,
        @Query("tz") timeZone: String): MuslimResponse<PrayerData>

    @GET("cal/hijr/{date}")
    suspend fun calendar(@Path("date") date: String, @Query("tz") timeZone: String,
        @Query("method") method: String = "standar"): MuslimResponse<CalendarData>

    @GET("cal/today")
    suspend fun calendarToday(@Query("tz") timeZone: String, @Query("method") method: String = "standar"): MuslimResponse<CalendarData>
}

interface GithubApi {
    @GET("repos/arbdevai/alquran-vip/releases/latest")
    suspend fun getLatestRelease(): GithubRelease
}
