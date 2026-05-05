package com.example.kronaerp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext




// Основные цвета бренда
private val PrimaryBlue = Color(0xFF25326A)     // Темно-синий (основной)
private val SecondaryBlue = Color(0xFF4A5E9A)   // Светло-синий
private val GoldAccent = Color(0xFFD4AF37)      // Золотой акцент
private val LightGold = Color(0xFFF1E8B0)       // Светло-золотой

/** Тёмная тема (профессиональная) */
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,                    // Белый текст на синем
    primaryContainer = SecondaryBlue,
    onPrimaryContainer = LightGold,

    secondary = Color(0xFF90A4AE),             // Серый-синий
    onSecondary = Color(0xFF263238),

    tertiary = GoldAccent,                     // Золотой акцент
    onTertiary = Color(0xFF1A1A1A),            // Тёмный на золотом

    background = Color(0xFF121212),            // Чёрный фон
    onBackground = Color(0xFFE0E0E0),          // Светлый текст

    surface = Color(0xFF1E1E1E),               // Карточки
    onSurface = Color(0xFFF5F5F5),

    error = Color(0xFFCF6679),
    onError = Color.White
)

/** Светлая тема (корпоративная) */
private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,

    primaryContainer = Color(0xFFE1E8FF),       // Светло-синий контейнер
    onPrimaryContainer = PrimaryBlue,

    secondary = SecondaryBlue,
    onSecondary = Color.White,

    tertiary = GoldAccent,
    onTertiary = Color(0xFF2C1810),            // Тёмный на золотом

    background = Color(0xFFF8FAFC),            // Светло-серый фон
    onBackground = Color(0xFF1A1B1F),

    surface = Color(0xFFFFFFFF),               // Белые карточки
    onSurface = Color(0xFF1C1B1F),

    error = Color(0xFFBA1A1A),
    onError = Color.White
)
@Composable
fun KronaERPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}