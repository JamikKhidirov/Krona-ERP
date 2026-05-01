package com.example.network.auth

import com.example.network.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
){

    private val ADMIN_ACCESS_CODE = "admin787898"

    suspend fun registerUser(
        email: String,
        pass: String,
        fio: String,
        role: String,
        orgCode: String
    ): Result<Unit> {
        return try {
            // ПРОВЕРКА: Если менеджер — проверяем код
            if (role == "Менеджер" && orgCode != ADMIN_ACCESS_CODE) {
                return Result.failure(Exception("Неверный код доступа организации"))
            }

            // Создаем аккаунт в Auth
            val authResult = auth.createUserWithEmailAndPassword(email, pass).await()
            val uid = authResult.user?.uid ?: throw Exception("Ошибка получения ID")

            // Создаем профиль (код организации в БД сохранять НЕ ОБЯЗАТЕЛЬНО, если он общий)
            val profile = UserProfile(
                uid = uid,
                fio = fio,
                login = email,
                role = role
            )

            db.collection("users").document(uid).set(profile).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    suspend fun signIn(login: String, pass: String): Result<String> {
        return try {
            // Превращаем логин в формат Firebase (как делали при регистрации)
            val emailFormat = if (login.contains("@")) login else "$login@krona.app"

            // 1. Авторизация
            val authResult = auth.signInWithEmailAndPassword(emailFormat, pass).await()
            val uid = authResult.user?.uid ?: throw Exception("Пользователь не найден")

            // 2. Получаем роль из Firestore
            val document = db.collection("users").document(uid).get().await()
            val role = document.getString("role") ?: "Client"

            Result.success(role)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}