package org.example.project.app.navigation.route

import kotlinx.serialization.Serializable
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.i24_calendar
import mobile_front.composeapp.generated.resources.i24_event
import mobile_front.composeapp.generated.resources.i24_profile
import mobile_front.composeapp.generated.resources.i24_tasks
import org.jetbrains.compose.resources.DrawableResource

@Serializable
sealed class TabRoute : Route {
    @Serializable
    data object ProfileTab : TabRoute()

    @Serializable
    data object FeedTab : TabRoute()

    @Serializable
    data object CalendarTab : TabRoute()

    @Serializable
    data object TasksTab : TabRoute()

    fun routeIcon(): DrawableResource {
        return when (this) {
            CalendarTab -> Res.drawable.i24_calendar
            FeedTab -> Res.drawable.i24_event
            ProfileTab -> Res.drawable.i24_profile
            TasksTab -> Res.drawable.i24_tasks
        }
    }
}