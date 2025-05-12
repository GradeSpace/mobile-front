package org.example.project.app.navigation.graph

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.example.project.app.navigation.route.TabRoute
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.features.feed.presentation.feed_list.FeedListScreenRoot
import org.example.project.features.feed.presentation.feed_list.FeedListViewModel
import org.example.project.features.lessons.presentation.calendar.CalendarScreenRoot
import org.example.project.features.lessons.presentation.calendar.CalendarViewModel
import org.example.project.features.tasks.presentation.tasks_list.TasksListScreenRoot
import org.example.project.features.tasks.presentation.tasks_list.TasksListViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.tabsNavGraph(
    navigationManager: NavigationManager,
    modifier: Modifier = Modifier
) {
    composable<TabRoute.FeedTab> {
        val viewModel = koinViewModel<FeedListViewModel>()
        FeedListScreenRoot(
            viewModel = viewModel,
            navigationManager = navigationManager,
            modifier = modifier
        )
    }
    composable<TabRoute.ProfileTab> {

    }
    composable<TabRoute.TasksTab> {
        val viewModel = koinViewModel<TasksListViewModel>()
        TasksListScreenRoot(
            viewModel = viewModel,
            navigationManager = navigationManager,
            modifier = modifier
        )
    }
    composable<TabRoute.CalendarTab> {
        val viewModel = koinViewModel<CalendarViewModel>()
        CalendarScreenRoot(
            viewModel = viewModel,
            navigationManager = navigationManager,
            modifier = modifier
        )
    }
}