package org.example.project.features.lessons.presentation.lesson

import org.example.project.core.presentation.UiText
import org.example.project.features.lessons.domain.LessonEventItem

data class LessonScreenState(
    val isRefreshing: Boolean = true,
    var error: UiText? = null,
    val lessonEventItem: LessonEventItem? = null
)
