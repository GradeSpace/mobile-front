package org.example.project.features.lessons.presentation.calendar

import org.example.project.app.navigation.route.Route

sealed interface CalendarScreenNavigationEvent {
    data class NavigateTo(val route: Route) : CalendarScreenNavigationEvent
}