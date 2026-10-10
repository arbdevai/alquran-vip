package com.arbdevai.quranvip.ui

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.ui.draw.scale
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
        // Screen Content spans full height so it scrolls BEHIND the floating dock
        Box(modifier = Modifier.fillMaxSize()) {
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

        if (state.reader == null) {
            FloatingIslandNavbar(
                currentScreen = state.screen,
                onScreenSelected = viewModel::setScreen,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
@Composable
private fun FloatingIslandNavbar(
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.padding(horizontal = 24.dp),
            shape = RoundedCornerShape(32.dp),
            color = Color(0xFFFAFAFA).copy(alpha = 0.95f),
            shadowElevation = 12.dp,
            border = BorderStroke(1.dp, Color(0xFFE5E7EB))
        ) {
            Row(
                modifier = Modifier
                    .height(64.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val navItems = listOf(
                    AppScreen.HOME to Icons.Default.Home,
                    AppScreen.QURAN to Icons.Default.MenuBook,
                    AppScreen.PRAYER to Icons.Default.AccessTime,
                    AppScreen.CALENDAR to Icons.Default.CalendarMonth,
                    AppScreen.TASBIH to Icons.Default.TouchApp
                )

                navItems.forEach { (screen, icon) ->
                    val selected = currentScreen == screen
                    val tint by animateColorAsState(if (selected) Color(0xFF0F172A) else Color(0xFF94A3B8), label = "tint")
                    val bgAlpha by animateFloatAsState(if (selected) 0.1f else 0f, label = "bg")
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { onScreenSelected(screen) }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color(0xFF0F172A).copy(alpha = bgAlpha)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
                        }
                    }
                }
            }
        }
    }
}
