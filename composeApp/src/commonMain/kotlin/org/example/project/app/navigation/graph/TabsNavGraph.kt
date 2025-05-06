package org.example.project.app.navigation.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.example.project.app.navigation.route.TabRoute
import org.example.project.app.navigation.utils.NavigationManager
import org.example.project.features.feed.presentation.feed_list.FeedListScreenRoot
import org.example.project.features.feed.presentation.feed_list.FeedListViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.tabsNavGraph(
    navigationManager: NavigationManager,
    innerPadding: PaddingValues
) {
    composable<TabRoute.FeedTab> {
        val viewModel = koinViewModel<FeedListViewModel>()
        FeedListScreenRoot(
            viewModel = viewModel,
            navigationManager = navigationManager,
            innerPadding = innerPadding
        )
    }
    composable<TabRoute.ProfileTab> {

    }
    composable<TabRoute.TasksTab> {

    }
    composable<TabRoute.CalendarTab> {

    }
}