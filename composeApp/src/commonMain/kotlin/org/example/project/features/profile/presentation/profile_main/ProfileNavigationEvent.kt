package org.example.project.features.profile.presentation.profile_main

import org.example.project.app.navigation.route.Route

sealed interface ProfileNavigationEvent {
    data class NavigateTo(val route: Route): ProfileNavigationEvent
}