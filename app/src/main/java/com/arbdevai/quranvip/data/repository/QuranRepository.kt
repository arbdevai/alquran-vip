package com.arbdevai.quranvip.data.repository

import com.arbdevai.quranvip.data.local.ResponseCache
import com.arbdevai.quranvip.data.model.Surah
import com.arbdevai.quranvip.data.model.TafsirSurah
import com.arbdevai.quranvip.data.remote.EQuranApi

class QuranRepository(private val api: EQuranApi, private val cache: ResponseCache) {
    private val TTL_SURAHS = 7 * 24 * 60 * 60 * 1000L // 7 days
    private val TTL_SURAH_DETAIL = 30 * 24 * 60 * 60 * 1000L // 30 days
    private val TTL_TAFSIR = 30 * 24 * 60 * 60 * 1000L // 30 days

    suspend fun getSurahs(): List<Surah> {
        return cache.get("quran_surahs", TTL_SURAHS) {
            api.surahs().data
        }
    }

    suspend fun getSurah(number: Int): Surah {
        return cache.get("quran_surah_$number", TTL_SURAH_DETAIL) {
            api.surah(number).data
        }
    }

    suspend fun getTafsir(number: Int): TafsirSurah {
        return cache.get("quran_tafsir_$number", TTL_TAFSIR) {
            api.tafsir(number).data
        }
    }
}
