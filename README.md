# Al-Qur'an VIP

Aplikasi Al-Qur'an Android berbahasa Indonesia dengan **Kotlin, Jetpack Compose, dan Material 3**. Desain menggunakan AMOLED Pure Dark, aksen amber, dan hero emerald sesuai [`style.md`](style.md).

> **Status:** Initial production baseline. APK dibangun dan ditandatangani oleh GitHub Actions—bukan dari build lokal.

## Fitur

- Daftar 114 surah dan pembaca ayat Arab, transliterasi, serta terjemahan Indonesia.
- Audio per ayat atau satu surah penuh melalui Media3, dengan enam qari dari EQuran v2.
- Bookmark dan posisi baca terakhir tersimpan di perangkat melalui DataStore.
- Ukuran teks Arab, transliterasi, serta terjemahan dapat disesuaikan.
- Jadwal salat dan kalender Hijriyah dari MyQuran API v3.
- Pemilihan lokasi manual dan deteksi GPS **hanya saat pengguna mengizinkan**. GPS diterjemahkan secara lokal ke kota/kabupaten; jika pemetaan tidak unik, aplikasi meminta pilihan manual dan tidak menebak jadwal.
- Tasbih digital dengan umpan balik haptik.

## Sumber Data

| Kebutuhan | Provider | Endpoint utama |
| --- | --- | --- |
| Al-Qur'an & audio qari | [EQuran.id v2](https://equran.id/apidev/v2) | `https://equran.id/api/v2/surat` |
| Jadwal salat & kalender | [MyQuran API Muslim v3](https://api.myquran.com/doc) | `https://api.myquran.com/v3/` |

Dokumentasi API Muslim v3 yang diterima saat pengembangan **tidak dilacak Git** (`apimuslim.json`) agar spesifikasi pihak ketiga tidak disebarkan ulang. Integrasi mengikuti versi `v3.1.3`, termasuk `sholat/kabkota`, `sholat/jadwal/{id}/{period}`, dan `cal/hijr/{date}`.

## Privasi Lokasi

- Izin lokasi diminta ketika tombol **Gunakan GPS** ditekan; aplikasi tetap bekerja dengan pemilihan kota manual.
- Koordinat tidak disimpan dan tidak dikirim ke EQuran ataupun MyQuran.
- MyQuran jadwal salat berbasis kota/kabupaten Indonesia. Aplikasi tidak menampilkan jadwal sampai kota berhasil dipilih.

## Persyaratan Platform

- `minSdk`: 29 (Android 10)
- `targetSdk` / `compileSdk`: 36 (Android 16)
- Java 17

## Build APK dengan GitHub Actions

Workflow [`.github/workflows/build-apk.yml`](.github/workflows/build-apk.yml) menjalankan unit test dan lint pada setiap push/PR. Push ke `main` menghasilkan signed release APK yang tersedia sebagai Actions artifact. Tag `v*` menerbitkan APK pada GitHub Release.

### Secret yang wajib disiapkan

Keystore **tidak boleh** di-commit. Tambahkan empat GitHub Actions secret berikut pada repository:

- `KEYSTORE_BASE64`: isi file `release.keystore` dalam base64 satu baris.
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

Contoh (jalankan di mesin yang memegang keystore, jangan commit output):

```bash
base64 --wrap=0 keystore/release.keystore | gh secret set KEYSTORE_BASE64
printf '%s' "$PASSWORD" | gh secret set KEYSTORE_PASSWORD
printf '%s' 'quranvip' | gh secret set KEY_ALIAS
printf '%s' "$PASSWORD" | gh secret set KEY_PASSWORD
```

`keystore/`, credential lokal, dan semua ekstensi keystore telah diabaikan oleh [`.gitignore`](.gitignore).

## Pengembangan

Gradle wrapper sudah disediakan. Bila ingin menjalankan Android Studio, pastikan Android SDK Platform 36 dan Build Tools 36 terpasang. Untuk menjaga kebijakan proyek, gunakan GitHub Actions untuk build APK rilis.

## Lisensi dan Ketergantungan Data

Periksa lisensi serta ketentuan penggunaan EQuran dan MyQuran sebelum distribusi komersial. Nama **VIP** tidak berarti pembelian atau akses berbayar; aplikasi ini tidak mengumpulkan data pembayaran.
