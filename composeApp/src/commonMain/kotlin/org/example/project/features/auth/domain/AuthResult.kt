package org.example.project.features.auth.domain

sealed class AuthResult {
    data object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
    data object UserNotFound : AuthResult()
    data object InvalidCredentials : AuthResult()
}
