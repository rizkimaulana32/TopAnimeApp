package com.c242ps518.topanimeairing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.c242ps518.topanimeairing.ui.theme.TopAnimeAiringTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TopAnimeAiringTheme(darkTheme = false) {
                TopAnimeAiring()
            }
        }
    }
}