package org.example.project.features.feed.navigation

import kotlinx.serialization.Serializable
import org.example.project.app.navigation.route.Route

sealed interface FeedRoute : Route {

    @Serializable
    data class FeedNotification(
        val eventId: String? = null
    ) : FeedRoute

    @Serializable
    data object NotificationCreate : FeedRoute
}