package org.example.project.features.lessons.presentation.lesson_create

import org.example.project.core.data.model.attachment.Attachment

sealed interface LessonCreateNavigationEvent {
    data object NavigateBack : LessonCreateNavigationEvent
    data class OpenFile(
        val attachment: Attachment
    ) : LessonCreateNavigationEvent
}
