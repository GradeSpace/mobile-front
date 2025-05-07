package org.example.project.features.feed.presentation.feed_list

import org.example.project.features.feed.domain.FeedEventItem

sealed interface FeedListAction {
    data class FeedListItemClick(val item: FeedEventItem) : FeedListAction
    data object OnPullToRefresh : FeedListAction
}