package org.example.project.features.auth.presentation.auth_screen

sealed interface AuthNavigationEvent {
    data class ShowError(val message: String) : AuthNavigationEvent
}
