package org.example.project.features.lessons.presentation.lesson_create

import org.example.project.core.presentation.UiSnackbar

sealed interface LessonCreateUiEvent {
    data class ShowErrorSnackbar(
        val snackbar: UiSnackbar
    ) : LessonCreateUiEvent
}
