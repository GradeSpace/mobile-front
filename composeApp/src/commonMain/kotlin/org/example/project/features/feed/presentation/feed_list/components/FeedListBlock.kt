package org.example.project.features.feed.presentation.feed_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.example.project.features.feed.domain.BlockType
import org.example.project.features.feed.domain.FeedEventItem

@Composable
fun FeedListBlock(
    title: String,
    events: List<FeedEventItem>,
    blockType: BlockType,
    onItemClick: (FeedEventItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(50), // Круглые края
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier
            .padding(bottom = 2.dp)
            .wrapContentWidth() // Ширина по содержимому
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 6.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
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