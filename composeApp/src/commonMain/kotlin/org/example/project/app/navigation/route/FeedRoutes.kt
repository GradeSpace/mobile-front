package org.example.project.app.navigation.route

import kotlinx.serialization.Serializable

sealed interface FeedRoutes : Route {
    @Serializable
    data object FeedMain : FeedRoutes
}