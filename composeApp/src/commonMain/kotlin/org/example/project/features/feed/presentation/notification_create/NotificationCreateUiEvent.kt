package org.example.project.features.feed.presentation.notification_create

import org.example.project.core.presentation.UiSnackbar

sealed interface NotificationCreateUiEvent {
    data class ShowErrorSnackbar(
        val snackbar: UiSnackbar
    ) : NotificationCreateUiEvent
}