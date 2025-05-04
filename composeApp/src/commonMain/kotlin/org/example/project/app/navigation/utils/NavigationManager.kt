package org.example.project.app.navigation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.CoroutineScope
import org.example.project.app.navigation.route.Route
import org.example.project.app.navigation.route.TabRoute

class NavigationManager(private val navController: NavHostController) {

    @Composable
    fun currentDestination(): NavDestination? {
        val backStackEntry by navController.currentBackStackEntryAsState()
        return backStackEntry?.destination
    }

    @Composable
    fun subscribeNavigationOnLifecycle(
        navEventsBlock: suspend CoroutineScope.() -> Unit
    ) {
        val lifecycleOwner = LocalLifecycleOwner.current
        LaunchedEffect(lifecycleOwner.lifecycle) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                navEventsBlock()
            }
        }
    }


    fun navigateBack() {
        navController.popBackStack()
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
            restoreState = true
            navController.graph.findStartDestination().route?.let { route ->
                popUpTo(route = route) {
                    saveState = true
                }
            }
        }
    }
}
