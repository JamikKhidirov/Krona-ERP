package com.example.kronaerp

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class KronaApp: Application() {

    override fun onCreate() {
        super.onCreate()
        val firebaseApp = FirebaseApp.initializeApp(this)
        if (firebaseApp == null) {
            Log.e("Firebase", "❌ Firebase НЕ инициализирован!")
        } else {
            Log.d("Firebase", "✅ Firebase инициализирован: ${firebaseApp.name}")
        }
    }

}