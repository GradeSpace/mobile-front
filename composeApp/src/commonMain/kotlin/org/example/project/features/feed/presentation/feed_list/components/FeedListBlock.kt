package org.example.project.features.feed.presentation.feed_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.features.feed.domain.BlockType
import org.example.project.features.feed.domain.FeedEventItem

context(LazyListScope)
@Composable
fun FeedListBlock(
    events: List<FeedEventItem>,
    blockType: BlockType,
    onItemClick: (FeedEventItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .widthIn(max = 600.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 12.dp)
    ) {
        events.forEach { event ->
            FeedListItem(event, blockType) {
                onItemClick(event)
            }
        }
    }
}