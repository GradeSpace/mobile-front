package org.example.project.app.navigation.graph

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.example.project.app.LocalNavigationManager
import org.example.project.app.navigation.route.TabRoute
import org.example.project.features.feed.presentation.feed_list.FeedListScreenRoot
import org.example.project.features.feed.presentation.feed_list.FeedListViewModel
import org.example.project.features.lessons.presentation.calendar.CalendarScreenRoot
import org.example.project.features.lessons.presentation.calendar.CalendarViewModel
import org.example.project.features.profile.presentation.profile_main.ProfileScreenRoot
import org.example.project.features.profile.presentation.profile_main.ProfileViewModel
import org.example.project.features.tasks.presentation.tasks_list.TasksListScreenRoot
import org.example.project.features.tasks.presentation.tasks_list.TasksListViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.tabsNavGraph(
    modifier: Modifier = Modifier
) {
    composable<TabRoute.FeedTab> {
        val viewModel = koinViewModel<FeedListViewModel>()
        val navigationManager = LocalNavigationManager.current
        FeedListScreenRoot(
            viewModel = viewModel,
            navigationManager = navigationManager,
            modifier = modifier
        )
    }
    composable<TabRoute.ProfileTab> {
        val viewModel = koinViewModel<ProfileViewModel>()
        val navigationManager = LocalNavigationManager.current
        ProfileScreenRoot(
            viewModel = viewModel,
            navigationManager = navigationManager,
            modifier = modifier
        )
    }
    composable<TabRoute.TasksTab> {
        val viewModel = koinViewModel<TasksListViewModel>()
        val navigationManager = LocalNavigationManager.current
        TasksListScreenRoot(
            viewModel = viewModel,
            navigationManager = navigationManager,
            modifier = modifier
        )
    }
    composable<TabRoute.CalendarTab> {
        val viewModel = koinViewModel<CalendarViewModel>()
        val navigationManager = LocalNavigationManager.current
        CalendarScreenRoot(
            viewModel = viewModel,
            navigationManager = navigationManager,
            modifier = modifier
        )
    }
}