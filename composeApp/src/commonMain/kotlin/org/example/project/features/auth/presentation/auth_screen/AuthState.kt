package org.example.project.features.auth.presentation.auth_screen

import org.example.project.core.data.model.user.UserRole

sealed class AuthState {
    data object Initial : AuthState()
    data object Loading : AuthState()
    data object Authenticated : AuthState()
    data class EmailEntered(val email: String) : AuthState()
    data class PasswordRequired(
        val identifier: String,
        val isPhone: Boolean = false
    ) : AuthState()
    data class RegistrationRequired(
        val identifier: String,
        val isPhone: Boolean = false,
        val selectedRole: UserRole? = null,
        val firstName: String = "",
        val lastName: String = "",
        val middleName: String = "",
        val group: String = "",
        val password: String = "",
        val confirmPassword: String = ""
    ) : AuthState()
    data class Error(val message: String) : AuthState()
}
