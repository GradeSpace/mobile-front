package org.example.project.app.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import org.example.project.app.navigation.route.Graph
import org.example.project.app.navigation.route.ProfileRoutes
import org.example.project.app.navigation.route.TabRoute
import org.example.project.features.feed.navigation.FeedRoute
import org.example.project.features.feed.navigation.feedNavGraph
import org.example.project.features.lessons.navigation.LessonRoutes
import org.example.project.features.lessons.navigation.lessonsNavGraph
import org.example.project.features.tasks.navigation.TasksRoute
import org.example.project.features.tasks.navigation.tasksNavGraph

@Composable
fun RootNavGraph(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Graph.TabsGraph,
    ) {
        navigation<Graph.TabsGraph>(
            startDestination = TabRoute.FeedTab
        ) {
            tabsNavGraph(modifier)
        }

        navigation<Graph.FeedGraph>(
            startDestination = FeedRoute.FeedNotification()
        ) {
            feedNavGraph()
        }

        navigation<Graph.ProfileGraph>(
            startDestination = ProfileRoutes.ProfileMain
        ) {
            profileNavGraph()
        }

        navigation<Graph.TasksGraph>(
            startDestination = TasksRoute.TaskScreen()
        ) {
            tasksNavGraph()
        }

        navigation<Graph.CalendarGraph>(
            startDestination = LessonRoutes.Lesson()
        ) {
            lessonsNavGraph()
        }
    }
}