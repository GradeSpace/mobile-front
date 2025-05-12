package org.example.project.core.presentation.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.cab
import mobile_front.composeapp.generated.resources.description_title
import mobile_front.composeapp.generated.resources.online
import org.example.project.core.data.utils.buildTimeDiap
import org.example.project.core.data.utils.formatDateTime
import org.example.project.core.data.utils.formatTime
import org.example.project.core.domain.EventItemTimeFormat
import org.example.project.core.presentation.UiText
import org.jetbrains.compose.resources.stringResource

@Composable
fun EventDescription(
    description: UiText,
    lastUpdateTime: LocalDateTime,
    timeFormat: EventItemTimeFormat = EventItemTimeFormat.Full,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = { },
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(Res.string.description_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start
                )

                val timeAndLocationInfo = when (timeFormat) {
                    EventItemTimeFormat.Compact -> lastUpdateTime.formatTime()
                    EventItemTimeFormat.Full -> lastUpdateTime.formatDateTime()
                    is EventItemTimeFormat.Lesson -> buildString {
                        append(
                            buildTimeDiap(
                                timeFormat.startTime,
                                timeFormat.endTime
                            )
                        )
                        timeFormat.location?.cabinet?.let { cab ->
                            appendLine()
                            append("${stringResource(Res.string.cab)} $cab")
                        }
                        timeFormat.location?.lessonUrl?.let { url ->
                            appendLine()
                            append(stringResource(Res.string.online))
                        }
                    }
                }
                Column {
                    Text(
                        text = timeAndLocationInfo,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 3.dp)
                    )

                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 4.dp)
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Text(
                text = description.asString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            content()
        }
    }
}
