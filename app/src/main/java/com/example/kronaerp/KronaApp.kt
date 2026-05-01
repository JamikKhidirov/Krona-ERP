package com.example.kronaerp

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class KronaApp: Application() {

    override fun onCreate() {
        super.onCreate()
        // Явная инициализация Firebase ДО того, как Hilt начнёт инжектить зависимости
        FirebaseApp.initializeApp(this)
    }
}