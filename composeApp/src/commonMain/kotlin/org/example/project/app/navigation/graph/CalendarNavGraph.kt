package org.example.project.app.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.example.project.app.navigation.route.CalendarRoutes
import org.example.project.app.navigation.route.TabRoute
import org.example.project.app.navigation.utils.NavigationManager

fun NavGraphBuilder.calendarGraph(navigationManager: NavigationManager) {
    composable<CalendarRoutes.CalendarMain> {

    }
}