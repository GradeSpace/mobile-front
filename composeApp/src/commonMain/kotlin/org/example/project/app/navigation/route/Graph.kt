package org.example.project.app.navigation.route

import kotlinx.serialization.Serializable

sealed interface Graph {
    @Serializable
    data object FeedGraph : Graph
    @Serializable
    data object TasksGraph : Graph
    @Serializable
    data object CalendarGraph : Graph
    @Serializable
    data object ProfileGraph : Graph
    @Serializable
    data object TabsGraph: Graph
}