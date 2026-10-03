package com.arbdevai.quranvip.data.model

import kotlinx.serialization.Serializable

@Serializable
data class QuranResponse<T>(val code: Int, val message: String = "", val data: T)

@Serializable
data class MuslimResponse<T>(val status: Boolean, val message: String = "", val data: T? = null) {
    fun requireData(): T = data?.takeIf { status } ?: error(message.ifBlank { "Data tidak tersedia" })
}

@Serializable
data class Surah(
    val nomor: Int,
    val nama: String,
    val namaLatin: String,
    val jumlahAyat: Int,
    val tempatTurun: String,
    val arti: String,
    val deskripsi: String = "",
    val audioFull: Map<String, String> = emptyMap(),
    val ayat: List<Ayah> = emptyList()
)

@Serializable
data class Ayah(
    val nomorAyat: Int,
    val teksArab: String,
    val teksLatin: String,
    val teksIndonesia: String,
    val audio: Map<String, String> = emptyMap()
)

@Serializable
data class TafsirSurah(val nomor: Int, val tafsir: List<TafsirAyah>)
@Serializable
data class TafsirAyah(val ayat: Int, val teks: String)

object Reciters {
    val names = linkedMapOf(
        "01" to "Abdullah Al-Juhany",
        "02" to "Abdul Muhsin Al-Qasim",
        "03" to "Abdurrahman As-Sudais",
        "04" to "Ibrahim Al-Dossari",
        "05" to "Misyari Rasyid Al-Afasi",
        "06" to "Yasser Al-Dosari"
    )
}

@Serializable
data class City(val id: String, val lokasi: String)
@Serializable
data class PrayerData(val id: String, val kabko: String, val prov: String, val jadwal: Map<String, PrayerDay>)
@Serializable
data class PrayerDay(
    val tanggal: String,
    val imsak: String, val subuh: String, val terbit: String, val dhuha: String,
    val dzuhur: String, val ashar: String, val maghrib: String, val isya: String
) {
    fun times() = listOf("Imsak" to imsak, "Subuh" to subuh, "Terbit" to terbit,
        "Dhuha" to dhuha, "Zuhur" to dzuhur, "Asar" to ashar, "Magrib" to maghrib, "Isya" to isya)
}

@Serializable
data class CalendarData(val method: String, val adjustment: Int, val ce: CalendarDate, val hijr: CalendarDate)
@Serializable
data class CalendarDate(
    val today: String, val day: Int, val dayName: String,
    val month: Int, val monthName: String, val year: Int
)

@Serializable
data class Bookmark(val surah: Int, val ayah: Int, val name: String, val arabic: String, val translation: String)
@Serializable
data class ReadingPosition(val surah: Int, val ayah: Int, val name: String)

@Serializable
data class UserPreferences(
    val qori: String = "05",
    val fontSize: Int = 28,
    val showLatin: Boolean = true,
    val showTranslation: Boolean = true,
    val city: City? = null,
    val zone: String = "Asia/Jakarta",
    val lastRead: ReadingPosition? = null,
    val bookmarks: List<Bookmark> = emptyList(),
    val tasbih: Int = 0
)
