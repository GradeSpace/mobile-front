package org.example.project.features.lessons.presentation.lesson

import org.example.project.core.presentation.UiText
import org.example.project.features.lessons.domain.LessonEventItem

data class LessonScreenState(
    val isRefreshing: Boolean = true,
    val error: UiText? = null,
    val hasCreateButton: Boolean = false,
    val lessonEventItem: LessonEventItem? = null
)
