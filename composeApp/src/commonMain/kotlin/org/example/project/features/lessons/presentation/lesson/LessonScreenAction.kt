package org.example.project.features.lessons.presentation.lesson

sealed interface LessonScreenAction {
    data object OnBackClick : LessonScreenAction
    data object OnPullToRefresh : LessonScreenAction
    data object OpenLink : LessonScreenAction
}