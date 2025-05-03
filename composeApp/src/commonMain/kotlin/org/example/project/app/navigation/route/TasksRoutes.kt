package org.example.project.app.navigation.route

import kotlinx.serialization.Serializable

sealed interface TasksRoutes : Route {

    @Serializable
    data object TasksMain : TasksRoutes
}