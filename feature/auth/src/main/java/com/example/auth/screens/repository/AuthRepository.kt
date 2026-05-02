package com.example.auth.screens.repository

import com.example.network.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.tasks.await


@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {
    // Код доступа для менеджера (в реальном проекте храни в Remote Config или Cloud Functions)
    private val MANAGER_ACCESS_CODE = "admin787898"

    /**
     * Регистрация нового пользователя
     * @param login — логин пользователя (без @)
     * @param pass — пароль
     * @param fio — ФИО
     * @param role — "Клиент" или "Менеджер"
     * @param orgCode — код доступа (только для Менеджера)
     */
    suspend fun registerUser(
        login: String,
        pass: String,
        fio: String,
        role: String,
        orgCode: String
    ): Result<Unit> {
        return try {
            // 1. Валидация роли
            if (role !in listOf("Клиент", "Менеджер")) {
                return Result.failure(Exception("Неверная роль"))
            }

            // 2. Проверка кода для Менеджера
            if (role == "Менеджер" && orgCode != MANAGER_ACCESS_CODE) {
                return Result.failure(Exception("Неверный код доступа организации"))
            }

            // 3. Формируем email для Firebase Auth
            val email = formatLoginToEmail(login)

            // 4. Создаём аккаунт в Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(email, pass).await()
            val uid = authResult.user?.uid
                ?: return Result.failure(Exception("Ошибка получения ID пользователя"))

            // 5. Создаём профиль в Firestore
            val profile = UserProfile(
                uid = uid,
                fio = fio,
                login = login,          // Сохраняем оригинальный логин
                email = email,            // Сохраняем полный email
                role = role,
                createdAt = System.currentTimeMillis()
            )

            db.collection("users").document(uid).set(profile).await()

            Result.success(Unit)
        } catch (e: FirebaseAuthWeakPasswordException) {
            Result.failure(Exception("Пароль слишком простой. Минимум 6 символов"))
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(Exception("Пользователь с таким логином уже существует"))
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка регистрации: ${e.localizedMessage}"))
        }
    }

    /**
     * Вход в систему
     * @param login — логин или email
     * @param pass — пароль
     * @return роль пользователя ("Клиент" или "Менеджер")
     */
    suspend fun signIn(login: String, pass: String): Result<String> {
        return try {
            val email = formatLoginToEmail(login)

            // 1. Авторизация в Firebase Auth
            val authResult = auth.signInWithEmailAndPassword(email, pass).await()
            val uid = authResult.user?.uid
                ?: return Result.failure(Exception("Пользователь не найден"))

            // 2. Получаем роль из Firestore
            val document = db.collection("users").document(uid).get().await()

            if (!document.exists()) {
                return Result.failure(Exception("Профиль пользователя не найден"))
            }

            val role = document.getString("role") ?: "Клиент"
            Result.success(role)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(Exception("Неверный логин или пароль"))
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(Exception("Пользователь не найден"))
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка входа: ${e.localizedMessage}"))
        }
    }

    /** Выход из системы */
    fun signOut() {
        auth.signOut()
    }

    /** Получить текущего пользователя */
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    /** Проверить, авторизован ли пользователь */
    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    /** Получить профиль текущего пользователя */
    suspend fun getCurrentUserProfile(): Result<UserProfile> {
        val uid = auth.currentUser?.uid
            ?: return Result.failure(Exception("Пользователь не авторизован"))

        return try {
            val doc = db.collection("users").document(uid).get().await()
            val profile = doc.toObject(UserProfile::class.java)
                ?: return Result.failure(Exception("Профиль не найден"))
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // === Вспомогательные методы ===

    private fun formatLoginToEmail(login: String): String {
        return if (login.contains("@")) login else "$login@krona.app"
    }
}