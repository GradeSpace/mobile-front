package org.example.project.features.auth.presentation.auth_screen

import org.example.project.core.data.model.user.UserRole

sealed interface AuthAction {
    data class EmailEntered(val email: String) : AuthAction
    data class PasswordEntered(val password: String) : AuthAction
    data class RoleSelected(val role: UserRole) : AuthAction
    data class FirstNameEntered(val firstName: String) : AuthAction
    data class LastNameEntered(val lastName: String) : AuthAction
    data class MiddleNameEntered(val middleName: String) : AuthAction
    data class GroupEntered(val group: String) : AuthAction
    data class PasswordForRegistrationEntered(val password: String) : AuthAction
    data class ConfirmPasswordEntered(val password: String) : AuthAction
    data object SubmitRegistration : AuthAction
    data object BackToEmailInput : AuthAction
}
