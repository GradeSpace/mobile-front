package org.example.project.features.lessons.presentation.lesson

sealed interface LessonScreenNavigationEvent {
    data object NavigateBack : LessonScreenNavigationEvent
    data class OpenLink(val url: String) : LessonScreenNavigationEvent
}