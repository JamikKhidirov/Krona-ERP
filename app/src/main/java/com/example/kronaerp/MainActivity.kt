package com.example.kronaerp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.example.kronaerp.ui.theme.KronaERPTheme
import com.example.navigation.KronaAppNavigation
import com.example.uikit.screens.viewmodel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var keepSplashScreenOn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition { keepSplashScreenOn }


        lifecycleScope.launch {
            delay(1500)
            keepSplashScreenOn = false
        }


        setContent {
            val themeViewModel = hiltViewModel<ThemeViewModel>()
            val isDarkMode by themeViewModel.isDarkMode.collectAsState()
            val useDynamicColor by themeViewModel.useDynamicColor.collectAsState()

            KronaERPTheme(
                darkTheme = isDarkMode,
                dynamicColor = useDynamicColor
            ) {
                KronaAppNavigation()
            }
        }
    }
}

