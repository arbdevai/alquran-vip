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
            CorporateBottomNavbar(
                currentScreen = state.screen,
                onScreenSelected = viewModel::setScreen,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun CorporateBottomNavbar(
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 16.dp,
        border = BorderStroke(1.dp, BorderSubtle.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(72.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val navItems = listOf(
                Pair(AppScreen.HOME, Icons.Default.Home) to "Beranda",
                Pair(AppScreen.QURAN, Icons.Default.MenuBook) to "Al-Qur'an",
                Pair(AppScreen.PRAYER, Icons.Default.AccessTime) to "Salat",
                Pair(AppScreen.CALENDAR, Icons.Default.CalendarMonth) to "Kalender",
                Pair(AppScreen.TASBIH, Icons.Default.TouchApp) to "Tasbih"
            )

            navItems.forEach { (item, label) ->
                val (screen, icon) = item
                CorporateNavItem(
                    icon = icon,
                    label = label,
                    selected = currentScreen == screen,
                    onClick = { onScreenSelected(screen) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CorporateNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tint by animateColorAsState(
        targetValue = if (selected) AmberAccent else TextSecondary,
        label = "navTint"
    )
    val indicatorHeight by animateFloatAsState(
        targetValue = if (selected) 4f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "navIndicator"
    )
    val interaction = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = tint,
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            )
        }
        
        // Top Animated Indicator
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .width(32.dp)
                .height(indicatorHeight.dp)
                .clip(RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
                .background(tint)
        )
    }
}
