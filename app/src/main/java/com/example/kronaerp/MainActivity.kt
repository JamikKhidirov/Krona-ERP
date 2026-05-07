package com.example.kronaerp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.auth.screens.login.LogInScreen
import com.example.auth.screens.register.RegisterScreen
import com.example.kronaerp.ui.theme.KronaERPTheme
import com.example.navigation.KronaAppNavigation
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
            delay(1500)  // Задержка 1.5 секунды
            keepSplashScreenOn = false
        }


        setContent {
            KronaERPTheme {
                KronaAppNavigation()
            }
        }
    }
}

