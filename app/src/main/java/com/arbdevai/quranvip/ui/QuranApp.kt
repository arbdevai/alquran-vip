package com.arbdevai.quranvip.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbdevai.quranvip.ui.screens.*
import com.arbdevai.quranvip.ui.theme.AmberAccent
import com.arbdevai.quranvip.ui.theme.BgCanvas
import com.arbdevai.quranvip.ui.theme.SurfaceCard
import com.arbdevai.quranvip.ui.theme.TextSecondary

@Composable
fun QuranApp(state: UiState, viewModel: AppViewModel) {
    BackHandler(enabled = state.reader != null) { viewModel.closeReader() }
    BackHandler(enabled = state.reader == null && state.screen != AppScreen.HOME) {
        viewModel.setScreen(AppScreen.HOME)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgCanvas)
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (state.reader == null) 76.dp else 0.dp)
        ) {
            when (state.screen) {
                AppScreen.HOME -> HomeScreen(state, viewModel)
                AppScreen.QURAN -> QuranScreen(state, viewModel)
                AppScreen.PRAYER -> PrayerScreen(state, viewModel)
                AppScreen.BOOKMARKS -> BookmarksScreen(state, viewModel)
                AppScreen.SETTINGS -> SettingsScreen(state, viewModel)
                AppScreen.TASBIH -> TasbihScreen(state, viewModel)
            }
        }

        if (state.reader == null) {
            CalmBottomDock(
                currentScreen = state.screen,
                onScreenSelected = viewModel::setScreen,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            )
        }
    }
}

@Composable
private fun CalmBottomDock(
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = RoundedCornerShape(22.dp),
        color = SurfaceCard,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DockItem(Icons.Default.Home, "Beranda", currentScreen == AppScreen.HOME) { onScreenSelected(AppScreen.HOME) }
            DockItem(Icons.Default.MenuBook, "Al-Qur'an", currentScreen == AppScreen.QURAN) { onScreenSelected(AppScreen.QURAN) }
            DockItem(Icons.Default.AccessTime, "Jadwal", currentScreen == AppScreen.PRAYER) { onScreenSelected(AppScreen.PRAYER) }
            DockItem(Icons.Default.Bookmark, "Simpan", currentScreen == AppScreen.BOOKMARKS) { onScreenSelected(AppScreen.BOOKMARKS) }
            DockItem(Icons.Default.TouchApp, "Tasbih", currentScreen == AppScreen.TASBIH) { onScreenSelected(AppScreen.TASBIH) }
        }
    }
}

@Composable
private fun DockItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val tint by animateColorAsState(
        targetValue = if (selected) AmberAccent else TextSecondary,
        label = "dockTint"
    )
    val interaction = remember { MutableInteractionSource() }
    Column(
        modifier = Modifier
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(horizontal = 9.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = label, tint = tint, modifier = Modifier.size(21.dp))
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            label,
            color = tint,
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
