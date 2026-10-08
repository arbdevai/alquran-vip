package com.arbdevai.quranvip.ui

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbdevai.quranvip.ui.screens.*
import com.arbdevai.quranvip.ui.theme.*

@Composable
fun QuranApp(state: UiState, viewModel: AppViewModel) {
    // Auto-prompt GPS permission on first launch for accurate prayer times
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            viewModel.detectLocation()
        } else {
            viewModel.locationPermissionDenied()
        }
    }

    LaunchedEffect(state.preferencesReady) {
        if (state.preferencesReady && state.preferences.city == null) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Multi-tier Back Button Navigation
    BackHandler(enabled = state.reader != null) {
        viewModel.closeReader()
    }
    BackHandler(enabled = state.reader == null && state.screen != AppScreen.HOME) {
        viewModel.setScreen(AppScreen.HOME)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgCanvas)
            .statusBarsPadding()
    ) {
        // Screen Content Container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (state.reader == null) 84.dp else 0.dp)
        ) {
            when (state.screen) {
                AppScreen.HOME -> HomeScreen(state, viewModel)
                AppScreen.QURAN -> QuranScreen(state, viewModel)
                AppScreen.PRAYER -> PrayerScreen(state, viewModel)
                AppScreen.CALENDAR -> CalendarScreen(state, viewModel)
                AppScreen.BOOKMARKS -> BookmarksScreen(state, viewModel)
                AppScreen.SETTINGS -> SettingsScreen(state, viewModel)
                AppScreen.TASBIH -> TasbihScreen(state, viewModel)
            }
        }

        // True Floating Island Navigation Bar (5 Main Navigation Destinations)
        if (state.reader == null) {
            FloatingIslandNavbar(
                currentScreen = state.screen,
                onScreenSelected = viewModel::setScreen,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }
    }
}

@Composable
private fun FloatingIslandNavbar(
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(66.dp)
            .border(
                BorderStroke(
                    1.dp,
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.22f),
                            Color.White.copy(alpha = 0.05f),
                            AmberAccent.copy(alpha = 0.35f)
                        )
                    )
                ),
                RoundedCornerShape(33.dp)
            ),
        shape = RoundedCornerShape(33.dp),
        color = GlassNavbar,
        shadowElevation = 16.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IslandNavItem(Icons.Default.Home, "Beranda", currentScreen == AppScreen.HOME) { onScreenSelected(AppScreen.HOME) }
            IslandNavItem(Icons.Default.MenuBook, "Al-Qur'an", currentScreen == AppScreen.QURAN) { onScreenSelected(AppScreen.QURAN) }
            IslandNavItem(Icons.Default.AccessTime, "Salat", currentScreen == AppScreen.PRAYER) { onScreenSelected(AppScreen.PRAYER) }
            IslandNavItem(Icons.Default.CalendarMonth, "Kalender", currentScreen == AppScreen.CALENDAR) { onScreenSelected(AppScreen.CALENDAR) }
            IslandNavItem(Icons.Default.TouchApp, "Tasbih", currentScreen == AppScreen.TASBIH) { onScreenSelected(AppScreen.TASBIH) }
        }
    }
}

@Composable
private fun IslandNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val tint by animateColorAsState(
        targetValue = if (selected) AmberAccent else TextSecondary,
        label = "navTint"
    )
    val interaction = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (selected) AmberAccent.copy(alpha = 0.20f) else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = tint,
            fontSize = 10.5.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
