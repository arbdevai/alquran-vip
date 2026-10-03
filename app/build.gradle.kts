import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

val signingProperties = Properties()
val signingPropertiesFile = rootProject.file("local-signing.properties")
if (signingPropertiesFile.exists()) {
    signingProperties.load(FileInputStream(signingPropertiesFile))
}

android {
    namespace = "com.arbdevai.quranvip"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.arbdevai.quranvip"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    signingConfigs {
        create("release") {
            val keystorePath = providers.gradleProperty("QURANVIP_KEYSTORE")
                .orElse(providers.environmentVariable("QURANVIP_KEYSTORE"))
                .orElse(signingProperties.getProperty("storeFile") ?: "")
                .get()
            if (keystorePath.isNotBlank()) {
                storeFile = rootProject.file(keystorePath)
                storePassword = providers.gradleProperty("QURANVIP_STORE_PASSWORD")
                    .orElse(providers.environmentVariable("QURANVIP_STORE_PASSWORD"))
                    .orElse(signingProperties.getProperty("storePassword") ?: "")
                    .get()
                keyAlias = providers.gradleProperty("QURANVIP_KEY_ALIAS")
                    .orElse(providers.environmentVariable("QURANVIP_KEY_ALIAS"))
                    .orElse(signingProperties.getProperty("keyAlias") ?: "")
                    .get()
                keyPassword = providers.gradleProperty("QURANVIP_KEY_PASSWORD")
                    .orElse(providers.environmentVariable("QURANVIP_KEY_PASSWORD"))
                    .orElse(signingProperties.getProperty("keyPassword") ?: "")
                    .get()
            }
        }
    }

    buildTypes {
        debug { applicationIdSuffix = ".debug" }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures { compose = true; buildConfig = true }
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
    testOptions { unitTests.isIncludeAndroidResources = true }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.session)
    implementation(libs.androidx.media3.ui)
    implementation(libs.gms.location)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.junit)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
