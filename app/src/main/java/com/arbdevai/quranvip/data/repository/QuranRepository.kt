package com.arbdevai.quranvip.data.repository

import com.arbdevai.quranvip.data.model.Surah
import com.arbdevai.quranvip.data.model.TafsirSurah
import com.arbdevai.quranvip.data.remote.EQuranApi

class QuranRepository(private val api: EQuranApi) {
    private var cachedSurahs: List<Surah>? = null
    private val surahDetails = mutableMapOf<Int, Surah>()
    private val tafsirs = mutableMapOf<Int, TafsirSurah>()

    suspend fun getSurahs(): List<Surah> {
        cachedSurahs?.let { return it }
        val response = api.surahs()
        return response.data.also { cachedSurahs = it }
    }

    suspend fun getSurah(number: Int): Surah {
        surahDetails[number]?.let { return it }
        val response = api.surah(number)
        return response.data.also { surahDetails[number] = it }
    }

    suspend fun getTafsir(number: Int): TafsirSurah {
        tafsirs[number]?.let { return it }
        val response = api.tafsir(number)
        return response.data.also { tafsirs[number] = it }
    }
}
