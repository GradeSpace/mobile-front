package org.example.project.core.domain

import kotlinx.datetime.LocalDateTime
import org.example.project.features.lessons.domain.LessonLocation

sealed interface EventItemTimeFormat {
    data object Compact : EventItemTimeFormat
    data object Full : EventItemTimeFormat
    data class Lesson(
        val endTime: LocalDateTime? = null,
        val location: LessonLocation? = null,
    ) : EventItemTimeFormat
}