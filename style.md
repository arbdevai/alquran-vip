# 📱 Quran VIP Professional Design System 2026
*(Jetpack Compose · Material 3 · AMOLED Pure Dark · Glassmorphism · Professional Typographic Hierarchy)*

Panduan sistem desain komprehensif berstandar industri untuk aplikasi Al-Qur'an modern. Menggabungkan estetika **AMOLED Pure Dark 2026**, **Frosted Glassmorphism**, dan **Standarisasi Hirarki Tipografi Dual-Script (Arab Naskh & Latin Sans-Serif)** untuk keterbacaan optimal tanpa silau mata (*eye-strain free*).

---

## 1. Prinsip Hirarki Desain & Visual Rhythm

Aplikasi Al-Qur'an memiliki tantangan unik: menyajikan dua sistem aksara yang berbeda secara berdampingan (Arab berharakat lengkap dan Latin terjemahan). Prinsip yang diterapkan:

1. **Hierarchy of Focus (Hirarki Perhatian)**:
   - **Tingkat 1 (Fokus Utama)**: Ayat Arab (`teksArab`) dengan ukuran modular terbesar, kontras putih murni (`TextPrimary` `#FFFFFF`), dan ruang vertikal yang lapang agar seluruh harakat (syaddah, fathatain, sukun, waslah) terbaca jernih.
   - **Tingkat 2 (Fokus Sekunder)**: Transliterasi Latin fonetik sebagai panduan pelafalan. Menggunakan warna emas pasir hangat (`TextTransliteration` `#E8C988`), gaya miring (*italic*), dan bobot medium. **Dilarang memakai warna amber neon terang untuk kalimat panjang** karena merusak keterbacaan dan melelahkan mata.
   - **Tingkat 3 (Fokus Tersier)**: Terjemahan Bahasa Indonesia sebagai pemahaman makna. Menggunakan warna netral kontras tinggi (`TextTranslation` `#C8CCD4`) dengan jarak baris 1.55x (*line-height* 22sp) agar nyaman dibaca seperti buku fisik.
   - **Tingkat 4 (Mikro/Metadata)**: Nomor ayat, status tajwid, waktu shalat, dan nama surah menggunakan skala kecil (*label small* 11sp) dengan kontras tereduksi (`TextSecondary` `#9AA0A6`).

2. **Rhythm Vertikal (Vertical Grid 4dp/8dp)**:
   Semua margin, padding, dan jarak antar elemen adalah kelipatan dari **4dp** atau **8dp** untuk konsistensi visual di segala rasio layar.

---

## 2. Palet Token Warna Terstandarisasi (`Color.kt`)

Dioptimalkan untuk panel OLED/AMOLED (konsumsi daya baterai 0% pada warna hitam `#000000`) dengan aksen elegan bernuansa Islami modern.

```kotlin
package com.arbdevai.quranvip.ui.theme

import androidx.compose.ui.graphics.Color

// ==========================================
// 1. Kanvas & Surface (AMOLED Pure Black)
// ==========================================
val BgCanvas = Color(0xFF000000)             // Hitam absolut 100% untuk hemat daya AMOLED
val SurfaceCard = Color(0xFF14171C)          // Kartu kontainer solid
val SurfaceInput = Color(0xFF1E2229)         // Search bar, dropdown, chip latar
val SurfacePill = Color(0xFF262B34)          // Badge nomor ayat, chip kategori
val BorderSubtle = Color(0xFF2B313A)         // Garis pemisah halus

// ==========================================
// 2. Aksentuasi Brand & Status
// ==========================================
val AmberAccent = Color(0xFFFF9E00)          // Aksen emas menyala untuk tombol aktif, badge utama
val AmberPressed = Color(0xFFE68E00)         // State tombol saat ditekan
val EmeraldGradientStart = Color(0xFF0A7558) // Gradien hijau zamrud hero atas
val EmeraldGradientEnd = Color(0xFF139E77)   // Gradien hijau toska kanan-bawah

// ==========================================
// 3. Tipografi & Konten Teks Terstandar
// ==========================================
val TextPrimary = Color(0xFFFFFFFF)          // Putih murni untuk teks ayat Al-Qur'an
val TextTranslation = Color(0xFFC8CCD4)      // Abu-abu terang berdaya baca tinggi untuk terjemahan
val TextSecondary = Color(0xFF9AA0A6)        // Metadata, keterangan waktu, label sub-info
val TextTransliteration = Color(0xFFE8C988)  // Emas pasir lembut (eye-friendly) untuk transliterasi
val TextPlaceholder = Color(0xFF636B78)      // Hint teks pencarian

// ==========================================
// 4. Kategori Menu Beranda
// ==========================================
val CatQuran = Color(0xFF3B82F6)             // Biru samudra
val CatJadwal = Color(0xFF10B981)            // Emerald mint
val CatTasbih = Color(0xFFF97316)            // Oranye menyala
val CatTahlil = Color(0xFFEC4899)            // Magenta pink
val CatDoa = Color(0xFF14B8A6)               // Teal toska
val CatHadroh = Color(0xFF6366F1)            // Indigo violet

// ==========================================
// 5. Token Glassmorphism 2026
// ==========================================
val GlassBackground = Color(0xB8121418)       // Latar belakang frosted glass
val GlassSurface = Color(0xCC16191F)          // Kartu transparan dengan refleksi halus
val GlassNavbar = Color(0xF2121419)           // Floating bottom navbar kaca (94% opacity)
val GlassBorder = Color(0x2EFFFFFF)           // Border reflektif specular putih 18%
val GlassBorderGlow = Color(0x40FF9E00)       // Border berpendar aksen emas saat aktif
val GlassHighlight = Color(0x14FFFFFF)        // Pencahayaan bibir atas kartu (rim lighting)
val GlassCardPressed = Color(0xDE1F232B)      // State kartu saat ditekan / ayat diputar
```

---

## 3. Sistem Tipografi Terstandarisasi (`Type.kt`)

Sistem dual-family dengan font asli yang dibundel di dalam aplikasi:
- **Teks Arab**: `Amiri Regular` & `Amiri Bold` (`res/font/amiri_regular.ttf`, `res/font/amiri_bold.ttf`).
- **UI Latin**: `Plus Jakarta Sans` (`res/font/jakarta_sans.ttf`).

### Aturan Render Teks Arab:
1. Menggunakan `textDirection = TextDirection.Rtl` wajib pada semua blok ayat.
2. `lineHeightStyle` disetel ke `Alignment.Center` dan `Trim.None` agar harakat syaddah dan fathatain tidak terpotong tepi atas atau bawah.
3. Rasio tinggi baris Arab: **1.95x - 2.0x dari ukuran font**.

```kotlin
package com.arbdevai.quranvip.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import com.arbdevai.quranvip.R

val ArabicFontFamily = FontFamily(
    Font(R.font.amiri_regular, FontWeight.Normal),
    Font(R.font.amiri_bold, FontWeight.Bold)
)

val LatinFontFamily = FontFamily(
    Font(R.font.jakarta_sans, FontWeight.Normal),
    Font(R.font.jakarta_sans, FontWeight.Medium),
    Font(R.font.jakarta_sans, FontWeight.SemiBold),
    Font(R.font.jakarta_sans, FontWeight.Bold)
)

val ArabicLineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None
)

val QuranTypography = Typography(
    // 1. Teks Utama Ayat Arab (Scalable 24sp - 48sp)
    displayMedium = TextStyle(
        fontFamily = ArabicFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 56.sp,
        textDirection = TextDirection.Rtl,
        lineHeightStyle = ArabicLineHeightStyle,
        color = TextPrimary
    ),
    // 2. Tulisan Kaligrafi Nama Surah di Header / List
    headlineMedium = TextStyle(
        fontFamily = ArabicFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 36.sp,
        textDirection = TextDirection.Rtl,
        color = AmberAccent
    ),
    // 3. Judul Hero Card
    titleLarge = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.15.sp,
        color = TextPrimary
    ),
    // 4. Nama Surah Latin & Judul Komponen
    titleMedium = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        color = TextPrimary
    ),
    // 5. Transliterasi Fonetik Latin
    bodyMedium = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.5.sp,
        lineHeight = 20.sp,
        color = TextTransliteration
    ),
    // 6. Terjemahan Bahasa Indonesia
    bodySmall = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        color = TextTranslation
    ),
    // 7. Eyebrow Tag / Kicker (Overline)
    labelMedium = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 10.5.sp,
        lineHeight = 14.sp,
        letterSpacing = 1.2.sp,
        color = Color.White
    ),
    // 8. Label Sub-info & Metadata
    labelSmall = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.5.sp,
        lineHeight = 16.sp,
        color = TextSecondary
    )
)
```

---

## 4. Standarisasi Komponen Hero Card 2026 (`QuranHeroBanner.kt`)

Setiap halaman diwajibkan memiliki Hero Card sebagai pengganti Top App Bar biasa dengan spesifikasi:
1. **Luminous Gradient Background**: Gradien warna kaya dengan kedalaman tinggi (Emerald, Celestial Blue, atau Midnight Gold).
2. **Dual-layer Specular Rim Border**: Border tipis `1.dp` dengan gradien putih 35% ke 8% untuk memberikan kesan potongan kaca premium (*chamfered glass edge*).
3. **Eyebrow Tag (Kicker)**: Badge di atas judul dengan huruf kapital, jarak antar huruf renggang (*tracked 1.2sp*), dan latar semi-transparan.
4. **Grand Title & Subtitle**: Tipografi tegas dengan kontras jelas.
5. **Interactive Trailing Content**: Tombol aksi cepat berkontainer kaca (misal: GPS toggle, ganti qari, filter).
6. **Bottom Stat Pills (Opsional)**: Deretan badge informasi ringkas di bawah (misal: "114 Surah", "30 Juz", "Makkiyah").

### Blueprint Arsitektur Hero Card:
```text
┌────────────────────────────────────────────────────────┐
│ [✦ EYEBROW BADGE]                     [TRAILING ACTION]│
│                                                        │
│ GRAND TITLE (Display / Title Large)                    │
│ Subtitle penjelasan halaman berdaya baca tinggi        │
│                                                        │
│ [pill 1]  [pill 2]  [pill 3]                           │
└────────────────────────────────────────────────────────┘
```

---

## 5. Komponen Glassmorphic Lainnya

1. **Floating Navigation Bar**:
   - Bentuk kapsul mengambang (`RoundedCornerShape(34.dp)`).
   - Melayang di atas kanvas dengan margin sisi `20.dp` dan bawah `12.dp`.
   - Menggunakan warna `GlassNavbar` (`#F2121419`) dengan border gradien specular.
   - Layar memiliki padding bawah `80.dp` agar konten tidak tertutup navbar.

2. **Floating Ayah Reader Controller**:
   - Mengambang di bagian bawah reader dengan fungsi ukuran huruf (`A-` / `A+`) dan tombol `Putar Surah`.

3. **GlassCard Reusable Component**:
   - Komponen kontainer standar untuk daftar surah, ayat, bookmark, dan jadwal salat dengan border reflektif dan background transparan.
