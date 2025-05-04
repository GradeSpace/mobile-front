package org.example.project.features.feed.navigation

import kotlinx.serialization.Serializable
import org.example.project.app.navigation.route.Route

sealed interface FeedRoute : Route {

    @Serializable
    data class FeedEventMain(
        val eventId: String? = null
    ) : FeedRoute
}