package org.example.project.features.tasks.presentation.task_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.i24_acute
import mobile_front.composeapp.generated.resources.i24_timer_off
import org.example.project.core.data.utils.formatDateTime
import org.jetbrains.compose.resources.vectorResource
import kotlin.time.Duration.Companion.seconds

@Composable
fun TaskDeadlineDisplay(
    deadline: LocalDateTime,
    modifier: Modifier = Modifier
) {
    val timeZone = TimeZone.currentSystemDefault()
    val deadlineInstant = remember(deadline) {
        deadline.toInstant(timeZone)
    }

    var remainingSeconds by remember { mutableLongStateOf(0L) }
    var countdownText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            val now = Clock.System.now()
            val remaining = deadlineInstant - now
            remainingSeconds = remaining.inWholeSeconds

            countdownText = when {
                remainingSeconds <= 0 -> "Дедлайн истек"
                remainingSeconds < 86400 -> { // Less than a day
                    val hours = remainingSeconds / 3600
                    val minutes = (remainingSeconds % 3600) / 60
                    val seconds = remainingSeconds % 60

                    // Format using LocalDateTime.Format
                    LocalDateTime(
                        year = 2000,
                        monthNumber = 1,
                        dayOfMonth = 1,
                        hour = hours.toInt(),
                        minute = minutes.toInt(),
                        second = seconds.toInt()
                    ).format(
                        LocalDateTime.Format {
                            hour()
                            char(':')
                            minute()
                            char(':')
                            second()
                        }
                    )
                }
                else -> {
                    val days = remainingSeconds / 86400
                    val hours = (remainingSeconds % 86400) / 3600
                    "$days дн. $hours ч."
                }
            }

            delay(1.seconds)
        }
    }

    val isExpired = remainingSeconds <= 0
    val isUrgent = !isExpired && remainingSeconds < 86400 // Less than 1 day

    val backgroundColor = when {
        isExpired -> MaterialTheme.colorScheme.error
        isUrgent -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.secondaryContainer
    }

    val contentColor = when {
        isExpired -> MaterialTheme.colorScheme.onError
        isUrgent -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onSecondaryContainer
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = backgroundColor,
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Icon(
                    imageVector = if (isExpired)
                        vectorResource(Res.drawable.i24_timer_off)
                    else
                        vectorResource(Res.drawable.i24_acute),
                    contentDescription = null,
                    tint = contentColor
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    if (isExpired) {
                        // For expired deadlines
                        Text(
                            text = "Дедлайн истек!",
                            style = MaterialTheme.typography.labelLarge,
                            color = contentColor
                        )

                        Text(
                            text = deadline.formatDateTime(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = contentColor
                        )
                    } else {
                        // For active deadlines
                        Text(
                            text = "Дедлайн: ${deadline.formatDateTime()}",
                            style = MaterialTheme.typography.labelLarge,
                            color = contentColor
                        )

                        Text(
                            text = "Осталось: $countdownText",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = contentColor
                        )
                    }
                }
            }
        }
    }
}
