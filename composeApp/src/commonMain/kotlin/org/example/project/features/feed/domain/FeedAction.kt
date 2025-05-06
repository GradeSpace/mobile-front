package org.example.project.features.feed.domain

import org.example.project.core.data.model.Action

sealed interface FeedAction {
    data class ButtonAction(
        val actionName: String,
        val action: Action
    ) : FeedAction

    data class PerformedAction(
        val title: String
    ) : FeedAction
}
