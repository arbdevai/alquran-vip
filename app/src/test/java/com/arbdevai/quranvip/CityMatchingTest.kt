package com.arbdevai.quranvip

import com.arbdevai.quranvip.data.model.City
import com.arbdevai.quranvip.data.repository.CityMatching
import org.junit.Assert.*
import org.junit.Test

class CityMatchingTest {
    private val cities = listOf(
        City("1", "KOTA BANDUNG"),
        City("2", "KAB. BANDUNG"),
        City("3", "KAB. BANDUNG BARAT"),
        City("4", "KOTA MAKASSAR")
    )

    @Test fun exactKotaDoesNotChooseKabupaten() {
        assertEquals("1", CityMatching.match("Kota Bandung", cities)?.id)
    }

    @Test fun ambiguousBareNameIsRejected() {
        assertNull(CityMatching.match("Bandung", cities))
    }

    @Test fun provinceMapsToOfficialIndonesianZone() {
        assertEquals("Asia/Jakarta", CityMatching.zone("Jawa Barat"))
        assertEquals("Asia/Makassar", CityMatching.zone("Sulawesi Selatan"))
        assertEquals("Asia/Jayapura", CityMatching.zone("Papua Barat"))
    }
}
