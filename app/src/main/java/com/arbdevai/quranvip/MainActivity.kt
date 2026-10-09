package com.arbdevai.quranvip

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arbdevai.quranvip.ui.AppViewModel
import com.arbdevai.quranvip.ui.QuranApp
import com.arbdevai.quranvip.ui.theme.QuranVipTheme

class MainActivity : ComponentActivity() {
    private lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        appViewModel = ViewModelProvider(this)[AppViewModel::class.java]

        setContent {
            QuranVipTheme {
                val state by appViewModel.state.collectAsStateWithLifecycle()
                QuranApp(state = state, viewModel = appViewModel)
            }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (::appViewModel.isInitialized && event.action == KeyEvent.ACTION_DOWN) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP, KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    if (appViewModel.handleVolumeKeyAsTasbih()) {
                        return true
                    }
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }
}
