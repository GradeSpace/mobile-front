package org.example.project.features.lessons.presentation.calendar

import kotlinx.datetime.LocalDate
import org.example.project.features.lessons.domain.LessonEventItem

sealed interface CalendarScreenAction {
    data object OnPullToRefresh: CalendarScreenAction
    data class OnLessonClick(val lesson: LessonEventItem): CalendarScreenAction
    data class OnSelectedDayChange(val date: LocalDate): CalendarScreenAction
}