package com.arbdevai.quranvip.data.repository

import com.arbdevai.quranvip.data.model.City
import java.util.Locale

object CityMatching {
    private fun normalized(value: String) = value.uppercase(Locale.ROOT)
        .replace(Regex("^(KABUPATEN|KAB\\.?|REGENCY)\\s+"), "KAB ")
        .replace(Regex("\\s+REGENCY$"), "")
        .replace(Regex("\\s+CITY$"), "")
        .replace(Regex("\\s+"), " ").trim()

    fun match(name: String, cities: List<City>): City? {
        val candidate = normalized(name)
        if (candidate.isBlank()) return null
        cities.filter { normalized(it.lokasi) == candidate }.singleOrNull()?.let { return it }
        fun stem(s: String) = s.removePrefix("KOTA ").removePrefix("KAB ")
        // Never choose the first of a kabupaten/kota pair with the same name.
        return cities.filter { stem(normalized(it.lokasi)) == stem(candidate) }.singleOrNull()
    }

    fun zone(province: String): String? {
        val p = normalized(province)
        return when {
            p.contains("PAPUA") || p.contains("MALUKU") -> "Asia/Jayapura"
            p.contains("SULAWESI") || p.contains("GORONTALO") || p.contains("BALI") ||
                p.contains("NUSA TENGGARA") || p in setOf("KALIMANTAN SELATAN", "KALIMANTAN TIMUR", "KALIMANTAN UTARA") -> "Asia/Makassar"
            p.contains("SUMAT") || p.contains("JAVA") || p.contains("JAWA") ||
                p.contains("JAKARTA") || p.contains("YOGYAKARTA") || p.contains("ACEH") ||
                p.contains("RIAU") || p.contains("BANGKA") || p.contains("BANTEN") ||
                p.contains("BENGKULU") || p.contains("JAMBI") || p.contains("LAMPUNG") ||
                p in setOf("KALIMANTAN BARAT", "KALIMANTAN TENGAH") -> "Asia/Jakarta"
            else -> null
        }
    }
}
