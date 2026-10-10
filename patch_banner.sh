sed -i 's/arabicQuote: String? = null/arabicQuote: Triple<String, String, String>? = null/g' app/src/main/java/com/arbdevai/quranvip/ui/components/QuranHeroBanner.kt
sed -i 's/text = arabicQuote,/text = arabicQuote.first,/g' app/src/main/java/com/arbdevai/quranvip/ui/components/QuranHeroBanner.kt
cat << 'INNER_EOF' >> app/src/main/java/com/arbdevai/quranvip/ui/components/QuranHeroBanner.kt
// PLACEHOLDER
INNER_EOF
