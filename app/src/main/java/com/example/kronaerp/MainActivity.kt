package com.example.kronaerp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.auth.screens.login.LogInScreen
import com.example.auth.screens.register.RegisterScreen
import com.example.kronaerp.ui.theme.KronaERPTheme
import com.example.navigation.KronaAppNavigation
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val splashScreen = installSplashScreen()

        setContent {
            KronaERPTheme {
                KronaAppNavigation()
            }
        }
    }
}

