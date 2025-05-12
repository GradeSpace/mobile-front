package org.example.project.features.lessons.navigation

import kotlinx.serialization.Serializable
import org.example.project.app.navigation.route.Route

sealed interface LessonRoutes : Route {

    @Serializable
    data class Lesson(val eventId: String? = null) : LessonRoutes
}