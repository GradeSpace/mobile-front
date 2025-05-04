package org.example.project.app.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import org.example.project.app.navigation.route.CalendarRoutes
import org.example.project.features.feed.navigation.FeedRoute
import org.example.project.app.navigation.route.Graph
import org.example.project.app.navigation.route.ProfileRoutes
import org.example.project.app.navigation.route.TabRoute
import org.example.project.app.navigation.route.TasksRoutes
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.features.feed.navigation.feedNavGraph

@Composable
fun RootNavGraph(navController: NavHostController = rememberNavController()) {
    val navigationManager = remember {
        NavigationManager(navController)
    }

    NavHost(
        navController = navController,
        startDestination = Graph.TabsGraph,
    ) {
        navigation<Graph.TabsGraph>(
            startDestination = TabRoute.FeedTab
        ) {
            tabsNavGraph(navigationManager)
        }

        navigation<Graph.FeedGraph>(
            startDestination = FeedRoute.FeedEventMain()
        ) {
            feedNavGraph(navigationManager)
        }

        navigation<Graph.ProfileGraph>(
            startDestination = ProfileRoutes.ProfileMain
        ) {
            profileNavGraph(navigationManager)
        }

        navigation<Graph.TasksGraph>(
            startDestination = TasksRoutes.TasksMain
        ) {
            tasksNavGraph(navigationManager)
        }

        navigation<Graph.CalendarGraph>(
            startDestination = CalendarRoutes.CalendarMain
        ) {
            calendarGraph(navigationManager)
        }
    }
}