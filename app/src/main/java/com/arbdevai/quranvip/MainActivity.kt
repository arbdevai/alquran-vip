package com.arbdevai.quranvip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arbdevai.quranvip.ui.AppViewModel
import com.arbdevai.quranvip.ui.QuranApp
import com.arbdevai.quranvip.ui.theme.QuranVipTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuranVipTheme {
                val viewModel: AppViewModel = viewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                QuranApp(state = state, viewModel = viewModel)
            }
        }
    }
}
