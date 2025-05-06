package org.example.project.features.feed.presentation.feed_list

import org.example.project.app.navigation.route.Route

sealed interface FeedListNavigationEvent {
    data class NavigateTo(val route: Route) : FeedListNavigationEvent
}