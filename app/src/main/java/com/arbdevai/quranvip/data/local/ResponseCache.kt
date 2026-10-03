package com.arbdevai.quranvip.data.local

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json

@Entity(tableName = "responses")
data class CachedResponse(@PrimaryKey val cacheKey: String, val body: String, val savedAt: Long)
@Dao
interface ResponseDao {
    @Query("SELECT * FROM responses WHERE cacheKey = :key") suspend fun get(key: String): CachedResponse?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun put(value: CachedResponse)
}
@Database(entities = [CachedResponse::class], version = 1, exportSchema = false)
abstract class ResponseDatabase : RoomDatabase() {
    abstract fun responses(): ResponseDao
}
class ResponseCache(context: Context) {
    val json = Json { ignoreUnknownKeys = true }
    val dao = Room.databaseBuilder(context, ResponseDatabase::class.java, "responses.db").build().responses()
    val mutex = Mutex()
    suspend inline fun <reified T> get(key: String, ttl: Long, crossinline fetch: suspend () -> T): T = mutex.withLock {
        val stored = dao.get(key)
        if (stored != null && System.currentTimeMillis() - stored.savedAt < ttl) {
            try { return@withLock json.decodeFromString<T>(stored.body) } catch (_: IllegalArgumentException) { }
        }
        // No fallback to expired prayer/calendar records: surface an error instead.
        val value = fetch()
        dao.put(CachedResponse(key, json.encodeToString(kotlinx.serialization.serializer<T>(), value), System.currentTimeMillis()))
        value
    }
}
