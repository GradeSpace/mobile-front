package org.example.project.features.lessons.presentation.calendar

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.core.presentation.UiText
import org.example.project.features.lessons.domain.LessonBlocks

data class CalendarScreenState(
    val isRefreshing: Boolean = true,
    val error: UiText? = null,
    val lessonBlocks: LessonBlocks = emptyMap(),
    val startDate: LocalDate = LocalDate(
        year = 2025,
        monthNumber = 1,
        dayOfMonth = 1
    ),
    val endDate: LocalDate = LocalDate(
        year = 2026,
        monthNumber = 1,
        dayOfMonth = 1
    ),
    val selectedDay: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    val hasCreateButton: Boolean = false
)