package com.arbdevai.quranvip.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.arbdevai.quranvip.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore(name = "quran_vip_prefs")

class PreferencesRepository(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }

    private object Keys {
        val QORI = stringPreferencesKey("qori")
        val FONT_SIZE = intPreferencesKey("font_size")
        val SHOW_LATIN = booleanPreferencesKey("show_latin")
        val SHOW_TRANSLATION = booleanPreferencesKey("show_translation")
        val CITY = stringPreferencesKey("city")
        val TIME_ZONE = stringPreferencesKey("time_zone")
        val LAST_READ = stringPreferencesKey("last_read")
        val BOOKMARKS = stringPreferencesKey("bookmarks")
        val TASBIH = intPreferencesKey("tasbih")
        val TASBIH_TARGET = intPreferencesKey("tasbih_target")
        val TASBIH_HISTORY = stringPreferencesKey("tasbih_history")
    }

    val stream: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        val city = prefs[Keys.CITY]?.let { runCatching { json.decodeFromString<City>(it) }.getOrNull() }
        val lastRead = prefs[Keys.LAST_READ]?.let { runCatching { json.decodeFromString<ReadingPosition>(it) }.getOrNull() }
        val bookmarks = prefs[Keys.BOOKMARKS]?.let { runCatching { json.decodeFromString<List<Bookmark>>(it) }.getOrNull() } ?: emptyList()
        val history = prefs[Keys.TASBIH_HISTORY]?.let { runCatching { json.decodeFromString<List<TasbihHistory>>(it) }.getOrNull() } ?: emptyList()
        UserPreferences(
            qori = prefs[Keys.QORI] ?: "05",
            fontSize = prefs[Keys.FONT_SIZE] ?: 28,
            showLatin = prefs[Keys.SHOW_LATIN] ?: true,
            showTranslation = prefs[Keys.SHOW_TRANSLATION] ?: true,
            city = city,
            zone = prefs[Keys.TIME_ZONE] ?: "Asia/Jakarta",
            lastRead = lastRead,
            bookmarks = bookmarks,
            tasbih = prefs[Keys.TASBIH] ?: 0,
            tasbihTarget = prefs[Keys.TASBIH_TARGET] ?: 33,
            tasbihHistory = history
        )
    }

    suspend fun setZone(zone: String) = context.dataStore.edit { it[Keys.TIME_ZONE] = zone }
    suspend fun setQori(id: String) = context.dataStore.edit { it[Keys.QORI] = id }
    suspend fun setFontSize(size: Int) = context.dataStore.edit { it[Keys.FONT_SIZE] = size.coerceIn(20, 48) }
    suspend fun toggleLatin() = context.dataStore.edit { it[Keys.SHOW_LATIN] = !(it[Keys.SHOW_LATIN] ?: true) }
    suspend fun toggleTranslation() = context.dataStore.edit { it[Keys.SHOW_TRANSLATION] = !(it[Keys.SHOW_TRANSLATION] ?: true) }
    suspend fun setCity(city: City, zone: String) = context.dataStore.edit {
        it[Keys.CITY] = json.encodeToString(city)
        it[Keys.TIME_ZONE] = zone
    }
    suspend fun setLastRead(pos: ReadingPosition) = context.dataStore.edit {
        it[Keys.LAST_READ] = json.encodeToString(pos)
    }
    suspend fun toggleBookmark(bookmark: Bookmark) = context.dataStore.edit { prefs ->
        val current = prefs[Keys.BOOKMARKS]?.let { runCatching { json.decodeFromString<List<Bookmark>>(it) }.getOrNull() } ?: emptyList()
        val exists = current.any { it.surah == bookmark.surah && it.ayah == bookmark.ayah }
        val updated = if (exists) current.filterNot { it.surah == bookmark.surah && it.ayah == bookmark.ayah } else current + bookmark
        prefs[Keys.BOOKMARKS] = json.encodeToString(updated)
    }
    suspend fun incrementTasbih() = context.dataStore.edit { it[Keys.TASBIH] = (it[Keys.TASBIH] ?: 0) + 1 }
    suspend fun resetTasbih() = context.dataStore.edit { it[Keys.TASBIH] = 0 }
    suspend fun setTasbihTarget(target: Int) = context.dataStore.edit { it[Keys.TASBIH_TARGET] = target }
    suspend fun addTasbihHistory(entry: TasbihHistory) = context.dataStore.edit { prefs ->
        val current = prefs[Keys.TASBIH_HISTORY]?.let { runCatching { json.decodeFromString<List<TasbihHistory>>(it) }.getOrNull() } ?: emptyList()
        val updated = (listOf(entry) + current).take(25)
        prefs[Keys.TASBIH_HISTORY] = json.encodeToString(updated)
    }
    suspend fun clearTasbihHistory() = context.dataStore.edit { it.remove(Keys.TASBIH_HISTORY) }
}
