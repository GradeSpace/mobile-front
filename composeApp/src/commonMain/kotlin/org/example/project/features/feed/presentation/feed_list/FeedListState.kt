package org.example.project.features.feed.presentation.feed_list

import org.example.project.core.presentation.UiText
import org.example.project.features.feed.domain.FeedEventsBlock

data class FeedListState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: UiText? = null,
    val feedBlocks: List<FeedEventsBlock> = emptyList()
)