package com.arbdevai.quranvip.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.arbdevai.quranvip.ui.screens.*
import com.arbdevai.quranvip.ui.theme.BgCanvas
import com.arbdevai.quranvip.ui.theme.SurfaceCard
import com.arbdevai.quranvip.ui.theme.AmberAccent
import com.arbdevai.quranvip.ui.theme.TextSecondary

@Composable
fun QuranApp(state: UiState, viewModel: AppViewModel) {
    Scaffold(
        containerColor = BgCanvas,
        bottomBar = {
            if (state.reader == null) {
                NavigationBar(
                    containerColor = SurfaceCard,
                    contentColor = Color.White
                ) {
                    NavigationBarItem(
                        selected = state.screen == AppScreen.HOME,
                        onClick = { viewModel.setScreen(AppScreen.HOME) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
                        label = { Text("Beranda") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AmberAccent,
                            selectedTextColor = AmberAccent,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = Color.Transparent
                        )
                    )
                    NavigationBarItem(
                        selected = state.screen == AppScreen.QURAN,
                        onClick = { viewModel.setScreen(AppScreen.QURAN) },
                        icon = { Icon(Icons.Default.MenuBook, contentDescription = "Al-Qur'an") },
                        label = { Text("Al-Qur'an") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AmberAccent,
                            selectedTextColor = AmberAccent,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = Color.Transparent
                        )
                    )
                    NavigationBarItem(
                        selected = state.screen == AppScreen.PRAYER,
                        onClick = { viewModel.setScreen(AppScreen.PRAYER) },
                        icon = { Icon(Icons.Default.AccessTime, contentDescription = "Jadwal") },
                        label = { Text("Jadwal") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AmberAccent,
                            selectedTextColor = AmberAccent,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = Color.Transparent
                        )
                    )
                    NavigationBarItem(
                        selected = state.screen == AppScreen.BOOKMARKS,
                        onClick = { viewModel.setScreen(AppScreen.BOOKMARKS) },
                        icon = { Icon(Icons.Default.Bookmark, contentDescription = "Bookmark") },
                        label = { Text("Bookmark") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AmberAccent,
                            selectedTextColor = AmberAccent,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = Color.Transparent
                        )
                    )
                    NavigationBarItem(
                        selected = state.screen == AppScreen.TASBIH,
                        onClick = { viewModel.setScreen(AppScreen.TASBIH) },
                        icon = { Icon(Icons.Default.TouchApp, contentDescription = "Tasbih") },
                        label = { Text("Tasbih") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AmberAccent,
                            selectedTextColor = AmberAccent,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (state.screen) {
                AppScreen.HOME -> HomeScreen(state = state, viewModel = viewModel)
                AppScreen.QURAN -> QuranScreen(state = state, viewModel = viewModel)
                AppScreen.PRAYER -> PrayerScreen(state = state, viewModel = viewModel)
                AppScreen.BOOKMARKS -> BookmarksScreen(state = state, viewModel = viewModel)
                AppScreen.SETTINGS -> SettingsScreen(state = state, viewModel = viewModel)
                AppScreen.TASBIH -> TasbihScreen(state = state, viewModel = viewModel)
            }
        }
    }
}
