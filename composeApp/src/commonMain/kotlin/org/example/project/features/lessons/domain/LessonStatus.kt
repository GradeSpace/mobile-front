package org.example.project.features.lessons.domain

sealed interface LessonStatus {
    data object NotStarted : LessonStatus
    data object InProgress : LessonStatus
    data object Finished : LessonStatus
}