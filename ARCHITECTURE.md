# Al-Quran VIP - Project Architecture & Pattern Mapping

This document maps the architectural patterns and structure of the **Al-Quran VIP** project to serve as a guide for future development and to ensure consistency in the codebase.

## 1. High-Level Architecture
The project follows the **MVVM (Model-View-ViewModel)** architectural pattern combined with a unidirectional data flow (UDF) approach, heavily utilizing **Kotlin Coroutines** and **StateFlow**.

- **UI Framework**: Jetpack Compose (Declarative UI).
- **State Management**: `StateFlow` in a centralized ViewModel.
- **Language**: Kotlin.

## 2. Directory Structure

```text
app/src/main/java/com/arbdevai/quranvip/
├── audio/          # Background audio services and playback controllers (ExoPlayer/MediaPlayer)
├── data/           # Data layer (Models, Remote APIs, Local Cache, Repositories)
│   ├── local/      # PreferencesRepository, ResponseCache
│   ├── model/      # Data classes with kotlinx.serialization (@Serializable)
│   ├── remote/     # Network modules, API interfaces (EQuranApi)
│   └── repository/ # Repositories combining local and remote data sources
├── ui/             # Presentation layer
│   ├── components/ # Reusable Compose widgets (e.g., GlassCard, QuranAyahCard)
│   ├── screens/    # Main screen composables (HomeScreen, QuranScreens, UtilityScreens)
│   └── theme/      # Compose styling (Colors, Typography, Dimensions, Shapes)
├── util/           # Helper classes (e.g., CalendarHelper)
├── AppViewModel.kt # Centralized ViewModel managing UiState
├── MainActivity.kt # Entry point, sets up Compose and Volume Key interception
└── QuranVipApplication.kt # Manual Dependency Injection container
```

## 3. Core Patterns

### A. State Management & Unidirectional Data Flow
- **Single Source of Truth**: The app uses a single `AppViewModel` that manages a comprehensive `UiState` data class.
- **State Updates**: State is held in a `MutableStateFlow<UiState>` and exposed as an immutable `StateFlow<UiState>`.
- **UI Observation**: Compose screens observe the state using `collectAsStateWithLifecycle()` in `MainActivity.kt` and pass down necessary state slices to components.

### B. Dependency Injection (Manual DI)
Instead of using Dagger Hilt or Koin, the project uses **Manual Dependency Injection**.
- Instances of Repositories (`QuranRepository`, `PrayerRepository`, `LocationRepository`), `PreferencesRepository`, and `ResponseCache` are instantiated and held in `QuranVipApplication`.
- These dependencies are passed to the ViewModel or fetched via context casting when needed.

### C. Data Layer & Caching Strategy
- **Models**: Data transfer objects are annotated with `@Serializable` for `kotlinx.serialization`.
- **Repositories**: Repositories abstract the data origin (Network vs Local).
- **Caching**: `ResponseCache` is utilized to cache API responses (e.g., Surahs are cached for 7 days, Tafsir for 30 days) to allow offline usage and fast loading.

### D. Audio Playback
- Handled via `PlaybackController` and potentially bounded services (`QuranPlaybackService`) to allow Quran recitations to play in the background.

## 4. Development Guidelines (Rules for AI/Developers)

When adding new features or modifying the project, adhere to the following rules:
1. **UI Development**: Always use Jetpack Compose. Put reusable components in `ui/components/` and full-page views in `ui/screens/`.
2. **State Changes**: Do not mutate state directly in the UI. Send an intent/event to `AppViewModel`, and let it update the `_state.update { ... }`.
3. **Data Fetching**: Never call `NetworkModule` or APIs directly from the ViewModel. Always go through a Repository (e.g., `QuranRepository`) which handles caching logic via `ResponseCache`.
4. **Dependencies**: If you create a new Repository or Service, register it in `QuranVipApplication` and pass it down as needed.
5. **Serialization**: When adding new API models, use Kotlinx Serialization (`@Serializable`) instead of Gson/Moshi.
