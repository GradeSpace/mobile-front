package org.example.project.app.navigation.route

import kotlinx.serialization.Serializable

@Serializable
sealed class TabRoute(val icon: Int? = null) : Route {
    @Serializable
    data object ProfileTab : TabRoute()

    @Serializable
    data object FeedTab : TabRoute()

    @Serializable
    data object CalendarTab : TabRoute()

    @Serializable
    data object TasksTab : TabRoute()

}