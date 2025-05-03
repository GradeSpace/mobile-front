package org.example.project.app.navigation.route

import kotlinx.serialization.Serializable

sealed interface CalendarRoutes : Route {

    @Serializable
    data object CalendarMain : CalendarRoutes
}