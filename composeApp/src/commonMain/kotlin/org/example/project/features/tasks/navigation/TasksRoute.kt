package org.example.project.features.tasks.navigation

import kotlinx.serialization.Serializable
import org.example.project.app.navigation.route.Route

sealed interface TasksRoute : Route {

    @Serializable
    data class TaskScreen(
        val taskId: String? = null
    ) : TasksRoute
}