package org.example.project.features.tasks.presentation.task_create

import org.example.project.core.presentation.UiSnackbar

sealed interface TaskCreateUiEvent {
    data class ShowErrorSnackbar(
        val snackbar: UiSnackbar
    ) : TaskCreateUiEvent
}