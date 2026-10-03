# 📱 Quran App Design System (Jetpack Compose & Material 3)

Panduan sistem desain teknis untuk implementasi aplikasi Al-Qur'an menggunakan **Kotlin**, **Jetpack Compose**, dan **Material 3 (M3)** dengan tema **Modern AMOLED Dark Mode**.

---

## 1. Struktur Arsitektur Tema

Struktur paket tema Compose yang direkomendasikan pada modul `:app`:

```text
ui/theme/
├── Color.kt          // Definisi token warna & palet kategori
├── Type.kt           // Tipografi Latin + Arab (Amiri / Uthmanic)
├── Shape.kt          // Geometri & border-radius komponen
├── Dimensions.kt     // Spacing, icon size, padding standar
└── Theme.kt          // Konfigurasi MaterialTheme (DarkColorScheme)
```

---

## 2. Palet Warna (`Color.kt`)

Warna diekstrak langsung dari palet aplikasi, dioptimalkan untuk layar AMOLED dengan rasio kontras tinggi dan minim silau saat malam hari.

```kotlin
package com.example.quranapp.ui.theme

import androidx.compose.ui.graphics.Color

// ==========================================
// 1. Background & Surface (AMOLED Pure Dark)
// ==========================================
val BgCanvas = Color(0xFF000000)             // Root background layar
val SurfaceCard = Color(0xFF16181B)          // Kartu ayat, item surah, container pengaturan
val SurfaceInput = Color(0xFF22252A)         // Dropdown, search bar, secondary pill
val SurfacePill = Color(0xFF2A2E35)          // Nomor ayat badge, tafsir chip
val BorderSubtle = Color(0xFF2E3238)         // Garis pemisah halus

// ==========================================
// 2. Brand & Accent
// ==========================================
val AmberAccent = Color(0xFFFF9E00)          // CTA aktif, audio button, nomor surah, active tab
val AmberPressed = Color(0xFFE68E00)         // State tombol ditekan
val EmeraldGradientStart = Color(0xFF0A7558) // Banner atas & header surah
val EmeraldGradientEnd = Color(0xFF139E77)   // Banner gradien kanan-bawah

// ==========================================
// 3. Tipografi & Konten Teks
// ==========================================
val TextPrimary = Color(0xFFFFFFFF)          // Teks ayat Arab & judul tebal
val TextSecondary = Color(0xFF9E9E9E)        // Terjemahan & metadata surah
val TextTransliteration = Color(0xFFFF9E00)  // Kalimat transliterasi Latin fonetik
val TextPlaceholder = Color(0xFF656A72)      // Teks placeholder search

// ==========================================
// 4. Kategori Menu Beranda (Grid Icons)
// ==========================================
val CatQuran = Color(0xFF3B82F6)             // Biru
val CatJadwal = Color(0xFF10B981)            // Emerald mint
val CatTasbih = Color(0xFFF97316)            // Oranye terang
val CatTahlil = Color(0xFFEC4899)            // Magenta pink
val CatDoa = Color(0xFF14B8A6)               // Teal
val CatHadroh = Color(0xFF6366F1)            // Indigo / Ungu
```

---

## 3. Tipografi (`Type.kt`)

Sistem dual-family: teks Arab berbasis Naskh (`Amiri` / `LPMQ`) dan UI Latin Sans-Serif (`Plus Jakarta Sans` / `Inter`).

> **Catatan Asset:** Simpan berkas `.ttf` pada folder `res/font/`:
> * `amiri_regular.ttf` & `amiri_bold.ttf`
> * `jakarta_sans_regular.ttf`, `jakarta_sans_medium.ttf`, `jakarta_sans_semibold.ttf`, `jakarta_sans_bold.ttf`

```kotlin
package com.example.quranapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.quranapp.R

val ArabicFontFamily = FontFamily(
    Font(R.font.amiri_regular, FontWeight.Normal),
    Font(R.font.amiri_bold, FontWeight.Bold)
)

val LatinFontFamily = FontFamily(
    Font(R.font.jakarta_sans_regular, FontWeight.Normal),
    Font(R.font.jakarta_sans_medium, FontWeight.Medium),
    Font(R.font.jakarta_sans_semibold, FontWeight.SemiBold),
    Font(R.font.jakarta_sans_bold, FontWeight.Bold)
)

val QuranTypography = Typography(
    // 1. Teks Arab Ayat Al-Qur'an
    displayMedium = TextStyle(
        fontFamily = ArabicFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 52.sp,
        color = TextPrimary
    ),
    // 2. Tulisan Nama Surah Kaligrafi Arab di List
    headlineMedium = TextStyle(
        fontFamily = ArabicFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        color = AmberAccent
    ),
    // 3. Judul Halaman & Hero Banner
    titleLarge = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        color = TextPrimary
    ),
    // 4. Nama Surah (Latin) & Judul Menu
    titleMedium = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        color = TextPrimary
    ),
    // 5. Transliterasi Latin
    bodyMedium = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        color = TextTransliteration
    ),
    // 6. Terjemahan Indonesia
    bodySmall = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 19.sp,
        color = TextSecondary
    ),
    // 7. Label Sub-info, Metadata, Badge
    labelSmall = TextStyle(
        fontFamily = LatinFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        color = TextSecondary
    )
)
```

---

## 4. Bentuk & Spacing (`Shape.kt` & `Dimensions.kt`)

```kotlin
package com.example.quranapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val QuranShapes = Shapes(
    small = RoundedCornerShape(10.dp),      // Tombol kecil, badge action
    medium = RoundedCornerShape(16.dp),     // Card ayat, baris surah, menu box
    large = RoundedCornerShape(24.dp),      // Banner hero atas
    extraLarge = RoundedCornerShape(50.dp)  // Search bar kapsul, floating audio pill
)

object Spacing {
    val screenPadding = 16.dp
    val cardPadding = 16.dp
    val itemSpacing = 12.dp
    val internalSpacing = 8.dp
}
```

---

## 5. Tema Material 3 (`Theme.kt`)

```kotlin
package com.example.quranapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val QuranColorScheme = darkColorScheme(
    primary = AmberAccent,
    onPrimary = Color.Black,
    secondary = EmeraldGradientStart,
    onSecondary = Color.White,
    background = BgCanvas,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceInput,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle
)

@Composable
fun QuranAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = QuranColorScheme,
        typography = QuranTypography,
        shapes = QuranShapes,
        content = content
    )
}
```

---

## 6. Contoh Implementasi Composable Utama

### 6.1 Hero Banner (Inspirasi & Header Surah)
```kotlin
@Composable
fun QuranHeroBanner(
    title: String,
    subtitle: String,
    tag: String? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(
                Brush.linearGradient(
                    listOf(EmeraldGradientStart, EmeraldGradientEnd)
                )
            )
            .padding(Spacing.cardPadding)
    ) {
        Column {
            if (tag != null) {
                Text(
                    text = tag.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}
```

### 6.2 Kartu Baca Ayat (`QuranAyahCard`)
```kotlin
@Composable
fun QuranAyahCard(
    ayahNumber: Int,
    arabicText: String,
    latinText: String,
    translation: String,
    onPlayAudio: () -> Unit,
    onBookmark: () -> Unit,
    onTafsir: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.cardPadding)
        ) {
            // Row Aksi Header: [Nomor] ... [Bookmark] [Tafsir] [Play]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Badge Nomor Ayat
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(SurfacePill),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$ayahNumber",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }

                // Tombol Aksi Kanan
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBookmark,
                        modifier = Modifier.size(32.dp).background(SurfacePill, CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bookmark),
                            contentDescription = "Bookmark",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    FilledTonalButton(
                        onClick = onTafsir,
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = SurfacePill),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Tafsir", style = MaterialTheme.typography.labelSmall, color = Color.White)
                    }

                    IconButton(
                        onClick = onPlayAudio,
                        modifier = Modifier.size(32.dp).background(AmberAccent, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ayat Arab (Right to Left)
            Text(
                text = arabicText,
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Transliterasi Latin
            Text(
                text = latinText,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Terjemahan Bahasa Indonesia
            Text(
                text = translation,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
```

### 6.3 Floating Bottom Control (Ukuran Teks & Audio Global)
```kotlin
@Composable
fun FloatingQuranController(
    onFontDecrease: () -> Unit,
    onFontIncrease: () -> Unit,
    onPlayAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        color = SurfaceInput,
        tonalElevation = 6.dp,
        modifier = modifier.padding(bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(onClick = onFontDecrease) {
                Text("A-", color = AmberAccent, fontWeight = FontWeight.Bold)
            }
            TextButton(onClick = onFontIncrease) {
                Text("A+", color = AmberAccent, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onPlayAll,
                colors = ButtonDefaults.buttonColors(containerColor = AmberAccent),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Putar Semua", color = Color.Black, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
```
