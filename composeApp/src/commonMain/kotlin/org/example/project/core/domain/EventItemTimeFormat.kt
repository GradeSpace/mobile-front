package org.example.project.core.domain

import kotlinx.datetime.LocalDateTime
import org.example.project.core.data.model.event.EventLocation

sealed interface EventItemTimeFormat {
    data object Compact: EventItemTimeFormat

    data object Full : EventItemTimeFormat

    data class Lesson(
        val startTime: LocalDateTime? = null,
        val endTime: LocalDateTime? = null,
        val location: EventLocation? = null,
    ) : EventItemTimeFormat
}