package com.arbdevai.quranvip.util

import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField

object CalendarHelper {
    private val PASARAN = arrayOf("Legi", "Pahing", "Pon", "Wage", "Kliwon")
    private val ARABIC_DIGITS = arrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
    private val HIJRI_MONTHS = arrayOf(
        "Muharram", "Safar", "Rabi'ul Awal", "Rabi'ul Akhir",
        "Jumadil Awal", "Jumadil Akhir", "Rajab", "Sya'ban",
        "Ramadan", "Syawal", "Dzulqa'dah", "Dzulhijjah"
    )

    data class DayTriple(
        val gregorianDay: Int,
        val hijriDayArabic: String,
        val hijriDayNumber: Int,
        val hijriMonthName: String,
        val hijriYear: Int,
        val pasaran: String
    )

    fun getPasaran(date: LocalDate): String {
        val index = Math.floorMod(date.toEpochDay() + 3, 5L).toInt()
        return PASARAN[index]
    }

    fun toArabicNumber(number: Int): String {
        return number.toString().map { ARABIC_DIGITS[it.digitToInt()] }.joinToString("")
    }

    fun getDayTriple(date: LocalDate): DayTriple {
        val hijrahDate = HijrahDate.from(date)
        val hijriDay = hijrahDate.get(ChronoField.DAY_OF_MONTH)
        val hijriMonth = hijrahDate.get(ChronoField.MONTH_OF_YEAR)
        val hijriYear = hijrahDate.get(ChronoField.YEAR)
        val monthName = HIJRI_MONTHS.getOrElse(hijriMonth - 1) { "" }

        return DayTriple(
            gregorianDay = date.dayOfMonth,
            hijriDayArabic = toArabicNumber(hijriDay),
            hijriDayNumber = hijriDay,
            hijriMonthName = monthName,
            hijriYear = hijriYear,
            pasaran = getPasaran(date)
        )
    }
}
