package org.example.project.app.navigation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import org.example.project.app.navigation.route.CalendarRoutes
import org.example.project.app.navigation.route.FeedRoutes
import org.example.project.app.navigation.route.ProfileRoutes
import org.example.project.app.navigation.route.Route
import org.example.project.app.navigation.route.TabRoute
import org.example.project.app.navigation.route.TasksRoutes

class NavigationManager(private val navController: NavHostController) {

    @Composable
    fun currentDestination(): NavDestination? {
        val backStackEntry by navController.currentBackStackEntryAsState()
        return backStackEntry?.destination
    }

    fun navigateTo(route: Route) {
        when (route) {
            is TabRoute -> navigateToTab(route)
            else -> navController.navigate(route)
        }
    }

    private fun navigateToTab(tab: TabRoute) {
        navController.navigate(route = tab) {
            launchSingleTop = true
        }
    }


    fun navigateBack() {
        navController.popBackStack()
    }
}
