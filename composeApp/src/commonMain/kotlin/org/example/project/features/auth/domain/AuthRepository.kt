package org.example.project.features.auth.domain

import org.example.project.core.data.model.user.UserRole

interface AuthRepository {
    // Проверка статуса аутентификации
    suspend fun checkAuth(): Boolean

    // Аутентификация и регистрация
    suspend fun checkUserExists(email: String): Boolean
    suspend fun login(email: String, password: String): AuthResult
    suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        middleName: String?,
        role: UserRole,
        group: String?
    ): AuthResult

}
