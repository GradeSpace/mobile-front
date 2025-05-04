package org.example.project.app.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.example.project.app.navigation.route.TabRoute
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.features.feed.presentation.feed_list.FeedListScreenRoot
import org.example.project.features.feed.presentation.feed_list.FeedListViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.tabsNavGraph(navigationManager: NavigationManager) {
    composable<TabRoute.FeedTab> {
        val viewModel = koinViewModel<FeedListViewModel>()
        FeedListScreenRoot(
            viewModel = viewModel,
            navigationManager = navigationManager
        )
    }
    composable<TabRoute.ProfileTab> {

    }
    composable<TabRoute.TasksTab> {

    }
    composable<TabRoute.CalendarTab> {

    }
}