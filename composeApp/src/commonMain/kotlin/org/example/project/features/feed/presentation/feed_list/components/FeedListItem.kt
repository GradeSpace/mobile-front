package org.example.project.features.feed.presentation.feed_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.example.project.core.data.utils.formatDate
import org.example.project.core.data.utils.formatTime
import org.example.project.features.feed.domain.BlockType
import org.example.project.features.feed.domain.FeedEventItem

@Composable
fun FeedListItem(
    eventItem: FeedEventItem,
    blockType: BlockType,
    onClick: () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(

        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(bottom = 4.dp)
            ) {
                Text(
                    text = eventItem.author.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                )
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = eventItem.dateTime.formatTime(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium
                    )
                    if (blockType == BlockType.OLD) {
                        Text(
                            text = eventItem.dateTime.formatDate(withYear = true),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            Text(
                text = eventItem.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            eventItem.description?.let { description ->
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (blockType != BlockType.OLD && eventItem.actions.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(top = 8.dp)
                ) {
                    if (eventItem.actions.size > 1) {
                        eventItem.actions.forEach { action ->
                            FeedListItemActionButton(
                                title = action.actionName,
                                action = action.action,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            )
                        }
                    } else {
                        Spacer(
                            modifier = Modifier
                                .weight(0.5f)
                                .fillMaxWidth()
                        )
                        FeedListItemActionButton(
                            title = eventItem.actions[0].actionName,
                            action = eventItem.actions[0].action,
                            modifier = Modifier
                                .weight(0.5f)
                                .fillMaxWidth()
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                )
            } else {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                )
            }
        }
    }
}
