# Keep Kotlin serialization generated serializers and Retrofit DTOs.
-keepattributes *Annotation*
-keep class kotlinx.serialization.** { *; }
-keep class com.arbdevai.quranvip.data.model.** { *; }
-dontwarn okhttp3.**
-dontwarn org.conscrypt.**
