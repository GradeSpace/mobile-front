package org.example.project.features.tasks.presentation.tasks_list

import org.example.project.app.navigation.route.Route

sealed interface TasksListNavigationEvent {
    data class NavigateTo(val route: Route) : TasksListNavigationEvent
}