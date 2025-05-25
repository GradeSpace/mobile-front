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
    private val phoneToEmailMap = mutableMapOf<String, String>() // Для связи телефона с email

    init {
        // Добавляем тестовых пользователей
        val studentCredentials = UserCredentials(
            email = "student@example.com",
            phone = "+79001234567",
            password = "Password123",
            firstName = "Иван",
            lastName = "Студентов",
            middleName = "Иванович",
            role = UserRole.Student,
            group = "ИУ9-62Б"
        )

        val teacherCredentials = UserCredentials(
            email = "teacher@example.com",
            phone = "+79009876543",
            password = "Password123",
            firstName = "Петр",
            lastName = "Преподавателев",
            middleName = "Петрович",
            role = UserRole.Teacher,
            group = null
        )

        registeredUsers[studentCredentials.email] = studentCredentials
        registeredUsers[teacherCredentials.email] = teacherCredentials

        // Добавляем связь телефона с email
        phoneToEmailMap[studentCredentials.phone!!] = studentCredentials.email
        phoneToEmailMap[teacherCredentials.phone!!] = teacherCredentials.email
    }

    override suspend fun checkAuth(): Boolean {
        return userRepository.isAuthenticated()
    }

    override suspend fun checkUserExists(identifier: String): Boolean {
        delay(500) // Имитация сетевого запроса

        // Проверяем, существует ли пользователь с таким email или телефоном
        return if (isPhoneNumber(identifier)) {
            phoneToEmailMap.containsKey(identifier)
        } else {
            registeredUsers.containsKey(identifier)
        }
    }

    override suspend fun login(identifier: String, password: String): AuthResult {
        delay(1000) // Имитация сетевого запроса

        // Определяем email пользователя (если вход по телефону, находим соответствующий email)
        val email = if (isPhoneNumber(identifier)) {
            phoneToEmailMap[identifier]
        } else {
            identifier
        }

        // Если email не найден, значит пользователь не существует
        if (email == null) {
            return AuthResult.UserNotFound
        }

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
                userRepository.saveUserPhone(userCredentials.phone)
                userRepository.saveUserAuthStatus(true)

                AuthResult.Success
            }
        }
    }

    override suspend fun register(
        email: String,
        phone: String?,
        password: String,
        firstName: String,
        lastName: String,
        middleName: String?,
        role: UserRole,
        group: String?
    ): AuthResult {
        // Проверяем, существует ли пользователь с таким email
        if (email.isNotEmpty() && registeredUsers.containsKey(email)) {
            return AuthResult.Error("Пользователь с таким email уже существует")
        }

        // Проверяем, существует ли пользователь с таким телефоном
        if (phone != null && phoneToEmailMap.containsKey(phone)) {
            return AuthResult.Error("Пользователь с таким телефоном уже существует")
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
            phone = phone,
            password = password,
            firstName = firstName,
            lastName = lastName,
            middleName = middleName,
            role = role,
            group = group
        )

        registeredUsers[email] = userCredentials

        // Добавляем связь телефона с email, если телефон указан
        if (phone != null) {
            phoneToEmailMap[phone] = email
        }

        // Создаем объект пользователя
        val user = createUserFromCredentials(userCredentials)

        // Сохраняем данные пользователя через UserRepository
        userRepository.saveUser(user)
        userRepository.saveUserEmail(email)
        userRepository.saveUserPhone(phone)
        userRepository.saveUserAuthStatus(true)

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

    private fun isPhoneNumber(identifier: String): Boolean {
        return identifier.startsWith("+") && identifier.substring(1).all { it.isDigit() }
    }

    private data class UserCredentials(
        val email: String,
        val phone: String? = null,
        val password: String,
        val firstName: String,
        val lastName: String,
        val middleName: String?,
        val role: UserRole,
        val group: String?
    )
}
