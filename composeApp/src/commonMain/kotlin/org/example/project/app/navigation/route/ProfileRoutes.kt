package org.example.project.app.navigation.route

import kotlinx.serialization.Serializable

sealed interface ProfileRoutes : Route {

    @Serializable
    data object ProfileMain : ProfileRoutes
}