package org.example.project.features.feed.domain

import org.example.project.core.presentation.UiText

data class FeedEventsBlock(
    val id: Int,
    val events: List<FeedEventItem>,
    val title: List<UiText>,
    val blockType: BlockType
)

enum class BlockType {
    DEFAULT,
    OLD
}