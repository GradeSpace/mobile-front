package org.example.project.app.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.example.project.app.navigation.route.TabRoute
import org.example.project.app.navigation.route.TasksRoutes
import org.example.project.app.navigation.utils.NavigationManager

fun NavGraphBuilder.tasksNavGraph(navigationManager: NavigationManager) {
    composable<TasksRoutes.TasksMain> {

    }
}