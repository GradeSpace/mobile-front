package org.example.project.app.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import org.example.project.app.navigation.route.CalendarRoutes
import org.example.project.app.navigation.route.Graph
import org.example.project.app.navigation.route.ProfileRoutes
import org.example.project.app.navigation.route.TabRoute
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.features.feed.navigation.FeedRoute
import org.example.project.features.feed.navigation.feedNavGraph
import org.example.project.features.tasks.navigation.TasksRoute
import org.example.project.features.tasks.navigation.tasksNavGraph

@Composable
fun RootNavGraph(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
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
            tabsNavGraph(navigationManager, modifier)
        }

        navigation<Graph.FeedGraph>(
            startDestination = FeedRoute.FeedNotification()
        ) {
            feedNavGraph(navigationManager)
        }

        navigation<Graph.ProfileGraph>(
            startDestination = ProfileRoutes.ProfileMain
        ) {
            profileNavGraph(navigationManager)
        }

        navigation<Graph.TasksGraph>(
            startDestination = TasksRoute.TaskScreen()
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