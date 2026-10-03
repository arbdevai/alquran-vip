package com.arbdevai.quranvip.ui

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbdevai.quranvip.ui.screens.*
import com.arbdevai.quranvip.ui.theme.*

@Composable
fun QuranApp(state: UiState, viewModel: AppViewModel) {
    // 1. Back button handling:
    // If in reader: close reader and return to surah list
    BackHandler(enabled = state.reader != null) {
        viewModel.closeReader()
    }

    // If on a sub-screen (not HOME): return to HOME screen
    BackHandler(enabled = state.reader == null && state.screen != AppScreen.HOME) {
        viewModel.setScreen(AppScreen.HOME)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgCanvas)
    ) {
        // Screen content with bottom padding for floating navbar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (state.reader == null) 80.dp else 0.dp)
        ) {
            when (state.screen) {
                AppScreen.HOME -> HomeScreen(state = state, viewModel = viewModel)
                AppScreen.QURAN -> QuranScreen(state = state, viewModel = viewModel)
                AppScreen.PRAYER -> PrayerScreen(state = state, viewModel = viewModel)
                AppScreen.BOOKMARKS -> BookmarksScreen(state = state, viewModel = viewModel)
                AppScreen.SETTINGS -> SettingsScreen(state = state, viewModel = viewModel)
                AppScreen.TASBIH -> TasbihScreen(state = state, viewModel = viewModel)
            }
        }

        // 2. Floating Glassmorphism 2026 Navigation Bar
        if (state.reader == null) {
            FloatingGlassNavbar(
                currentScreen = state.screen,
                onScreenSelected = { viewModel.setScreen(it) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
private fun FloatingGlassNavbar(
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(68.dp)
            .border(
                BorderStroke(
                    1.dp,
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.22f),
                            Color.White.copy(alpha = 0.05f),
                            AmberAccent.copy(alpha = 0.28f)
                        )
                    )
                ),
                RoundedCornerShape(34.dp)
            ),
        shape = RoundedCornerShape(34.dp),
        color = GlassNavbar,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingNavItem(
                icon = Icons.Default.Home,
                label = "Beranda",
                isSelected = currentScreen == AppScreen.HOME,
                onClick = { onScreenSelected(AppScreen.HOME) }
            )
            FloatingNavItem(
                icon = Icons.Default.MenuBook,
                label = "Al-Qur'an",
                isSelected = currentScreen == AppScreen.QURAN,
                onClick = { onScreenSelected(AppScreen.QURAN) }
            )
            FloatingNavItem(
                icon = Icons.Default.AccessTime,
                label = "Jadwal",
                isSelected = currentScreen == AppScreen.PRAYER,
                onClick = { onScreenSelected(AppScreen.PRAYER) }
            )
            FloatingNavItem(
                icon = Icons.Default.Bookmark,
                label = "Bookmark",
                isSelected = currentScreen == AppScreen.BOOKMARKS,
                onClick = { onScreenSelected(AppScreen.BOOKMARKS) }
            )
            FloatingNavItem(
                icon = Icons.Default.TouchApp,
                label = "Tasbih",
                isSelected = currentScreen == AppScreen.TASBIH,
                onClick = { onScreenSelected(AppScreen.TASBIH) }
            )
        }
    }
}

@Composable
private fun FloatingNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val iconTint by animateColorAsState(
        targetValue = if (isSelected) AmberAccent else TextSecondary,
        label = "navIconTint"
    )

    Column(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(if (isSelected) AmberAccent.copy(alpha = 0.18f) else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal,
            color = iconTint
        )
    }
}
