package org.example.project.features.auth.data.repository

import kotlinx.coroutines.delay
import org.example.project.core.data.model.user.User
import org.example.project.core.data.model.user.UserRole
import org.example.project.core.data.model.user.UserStudent
import org.example.project.core.data.model.user.UserTeacher
import org.example.project.core.domain.repository.UserRepository
import org.example.project.features.auth.domain.AuthRepository
import org.example.project.features.auth.domain.AuthResult

class AuthMockRepository(
    private val userRepository: UserRepository,
) : AuthRepository {

    private val registeredUsers = mutableMapOf<String, UserCredentials>()

    init {
        // Добавляем тестовых пользователей
        registeredUsers["student@example.com"] = UserCredentials(
            email = "student@example.com",
            password = "Password123",
            firstName = "Иван",
            lastName = "Студентов",
            middleName = "Иванович",
            role = UserRole.Student,
            group = "ИУ9-62Б"
        )

        registeredUsers["teacher@example.com"] = UserCredentials(
            email = "teacher@example.com",
            password = "Password123",
            firstName = "Петр",
            lastName = "Преподавателев",
            middleName = "Петрович",
            role = UserRole.Teacher,
            group = null
        )
    }

    override suspend fun checkAuth(): Boolean {
        return userRepository.isAuthenticated()
    }

    override suspend fun checkUserExists(email: String): Boolean {
        delay(500) // Имитация сетевого запроса
        return registeredUsers.containsKey(email)
    }

    override suspend fun login(email: String, password: String): AuthResult {
        delay(1000) // Имитация сетевого запроса

        val userCredentials = registeredUsers[email]
        return when {
            userCredentials == null -> AuthResult.UserNotFound
            userCredentials.password != password -> AuthResult.InvalidCredentials
            else -> {
                // Создаем объект пользователя
                val user = createUserFromCredentials(userCredentials)

                // Сохраняем данные пользователя через UserRepository
                userRepository.saveUser(user)
                userRepository.saveUserEmail(email)
                userRepository.saveUserAuthStatus(true)

                AuthResult.Success
            }
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        middleName: String?,
        role: UserRole,
        group: String?
    ): AuthResult {

        if (registeredUsers.containsKey(email)) {
            return AuthResult.Error("Пользователь с таким email уже существует")
        }

        if (!isPasswordValid(password)) {
            return AuthResult.Error("Пароль должен содержать не менее 9 символов, включая хотя бы одну букву и одну цифру")
        }

        if (role == UserRole.Student && group.isNullOrBlank()) {
            return AuthResult.Error("Для студента необходимо указать группу")
        }

        delay(1500)

        val userCredentials = UserCredentials(
            email = email,
            password = password,
            firstName = firstName,
            lastName = lastName,
            middleName = middleName,
            role = role,
            group = group
        )

        registeredUsers[email] = userCredentials

        // Создаем объект пользователя
        val user = createUserFromCredentials(userCredentials)

        // Сохраняем данные пользователя через UserRepository
        userRepository.saveUser(user)
        userRepository.saveUserEmail(email)
        userRepository.saveUserAuthStatus(true)

        // Сохраняем токен авторизации
        return AuthResult.Success
    }

    private fun createUserFromCredentials(credentials: UserCredentials): User {
        val userId = credentials.email // Используем email как ID пользователя

        return when (credentials.role) {
            UserRole.Student -> {
                UserStudent(
                    name = credentials.firstName,
                    surname = credentials.lastName,
                    middleName = credentials.middleName,
                    uid = userId,
                    group = credentials.group ?: ""
                )
            }
            UserRole.Teacher -> {
                UserTeacher(
                    name = credentials.firstName,
                    surname = credentials.lastName,
                    middleName = credentials.middleName,
                    uid = userId
                )
            }
            else -> {
                User(
                    name = credentials.firstName,
                    surname = credentials.lastName,
                    middleName = credentials.middleName,
                    uid = userId,
                    role = credentials.role
                )
            }
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        if (password.length < 9) return false
        if (!password.any { it.isLetter() }) return false
        if (!password.any { it.isDigit() }) return false
        return true
    }

    private data class UserCredentials(
        val email: String,
        val password: String,
        val firstName: String,
        val lastName: String,
        val middleName: String?,
        val role: UserRole,
        val group: String?
    )
}
