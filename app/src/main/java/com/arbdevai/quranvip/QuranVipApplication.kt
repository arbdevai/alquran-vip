package com.arbdevai.quranvip

import android.app.Application
import com.arbdevai.quranvip.data.local.PreferencesRepository
import com.arbdevai.quranvip.data.local.ResponseCache
import com.arbdevai.quranvip.data.remote.NetworkModule
import com.arbdevai.quranvip.data.repository.LocationRepository
import com.arbdevai.quranvip.data.repository.PrayerRepository
import com.arbdevai.quranvip.data.repository.QuranRepository

class QuranVipApplication : Application() {
    lateinit var preferences: PreferencesRepository
        private set
    lateinit var responseCache: ResponseCache
        private set
    lateinit var quranRepository: QuranRepository
        private set
    lateinit var prayerRepository: PrayerRepository
        private set
    lateinit var locationRepository: LocationRepository
        private set

    override fun onCreate() {
        super.onCreate()
        preferences = PreferencesRepository(this)
        responseCache = ResponseCache(this)
        quranRepository = QuranRepository(NetworkModule.quran, responseCache)
        prayerRepository = PrayerRepository(NetworkModule.muslim, responseCache)
        locationRepository = LocationRepository(this, prayerRepository)
    }
}
