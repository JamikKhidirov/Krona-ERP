package com.example.kronaerp.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FCMTokenManager @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun saveToken(userId: String) {
        try {
            val token = FirebaseMessaging.getInstance().token.await()
            firestore.collection("users").document(userId)
                .update("fcmToken", token)
                .await()
        } catch (_: Exception) { }
    }

    suspend fun removeToken(userId: String) {
        try {
            firestore.collection("users").document(userId)
                .update("fcmToken", "")
                .await()
        } catch (_: Exception) { }
    }
}
