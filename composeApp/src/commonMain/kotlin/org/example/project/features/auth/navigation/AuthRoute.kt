package org.example.project.features.auth.navigation

import kotlinx.serialization.Serializable
import org.example.project.app.navigation.route.Route

sealed interface AuthRoute : Route {
    @Serializable
    data object AuthScreen : AuthRoute
}