package org.example.project.core.presentation.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
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
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.cab
import mobile_front.composeapp.generated.resources.online
import org.example.project.core.data.model.event.EventItem
import org.example.project.core.data.utils.formatDate
import org.example.project.core.data.utils.formatTime
import org.example.project.core.domain.EventItemTimeFormat
import org.jetbrains.compose.resources.stringResource

@Composable
fun EventListItem(
    eventItem: EventItem,
    timeFormat: EventItemTimeFormat,
    onClick: () -> Unit,
    colors: CardColors = CardDefaults.elevatedCardColors(),
    modifier: Modifier = Modifier,
    bottomContent: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(24.dp),
        colors = colors,
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
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
                    text = eventItem.author.toShortName(),
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
                    when (timeFormat) {

                        is EventItemTimeFormat.Lesson -> {
                            Text(
                                text = buildString {
                                    append(eventItem.lastUpdateDateTime.formatTime())
                                    timeFormat.endTime?.let {
                                        append(" – ${timeFormat.endTime.formatTime()}")
                                    }
                                },
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.labelMedium
                            )
                            timeFormat.location?.let { location ->
                                location.cabinet?.let { cab ->
                                    Text(
                                        text = "${stringResource(Res.string.cab)} $cab",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                                location.lessonUrl?.let { cab ->
                                    Text(
                                        text = stringResource(Res.string.online),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }

                        else -> {
                            Text(
                                text = eventItem.lastUpdateDateTime.formatTime(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.labelMedium
                            )
                            if (timeFormat is EventItemTimeFormat.Full) {
                                Text(
                                    text = eventItem.lastUpdateDateTime.formatDate(withYear = true),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
            Text(
                text = eventItem.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = eventItem.description.asString(),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            bottomContent()
        }
    }
}
