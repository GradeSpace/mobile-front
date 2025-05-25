package org.example.project.features.lessons.presentation.lesson_create.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.clear
import mobile_front.composeapp.generated.resources.end_time
import mobile_front.composeapp.generated.resources.i24_schedule
import mobile_front.composeapp.generated.resources.lesson_date
import mobile_front.composeapp.generated.resources.not_set
import mobile_front.composeapp.generated.resources.start_time
import org.example.project.core.data.utils.formatDate
import org.example.project.core.data.utils.formatTime
import org.example.project.core.presentation.ui.common.CalendarDialog
import org.example.project.core.presentation.ui.common.TimePickerDialog
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun LessonDateTimeSection(
    lessonDate: LocalDate?,
    startTime: LocalDateTime?,
    endTime: LocalDateTime?,
    onLessonDateChange: (LocalDate?) -> Unit,
    onStartTimeChange: (LocalDateTime?) -> Unit,
    onEndTimeChange: (LocalDateTime?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Дата занятия
        DateSelector(
            title = stringResource(Res.string.lesson_date),
            date = lessonDate,
            onDateChange = onLessonDateChange,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Время начала занятия
        TimeSelector(
            title = stringResource(Res.string.start_time),
            time = startTime,
            lessonDate = lessonDate,
            onTimeChange = onStartTimeChange,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Время окончания занятия
        TimeSelector(
            title = stringResource(Res.string.end_time),
            time = endTime,
            lessonDate = lessonDate,
            onTimeChange = onEndTimeChange
        )
    }
}

@Composable
private fun DateSelector(
    title: String,
    date: LocalDate?,
    onDateChange: (LocalDate?) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCalendarDialog by remember { mutableStateOf(false) }

    // Для диалога календаря
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val startDate = now.date
    val endDate = LocalDate(startDate.year + 1, startDate.monthNumber, startDate.dayOfMonth)

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable { showCalendarDialog = true }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = date?.formatDate() ?: stringResource(Res.string.not_set),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                )

                if (date != null) {
                    Text(
                        text = stringResource(Res.string.clear),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            onDateChange(null)
                        }
                    )
                }
            }
        }
    }

    // Диалог выбора даты
    if (showCalendarDialog) {
        CalendarDialog(
            date = date ?: now.date,
            startDate = startDate,
            endDate = endDate,
            onDateChange = { epochMillis ->
                if (epochMillis != null) {
                    val localDateTime = Instant
                        .fromEpochMilliseconds(epochMillis)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                    onDateChange(localDateTime.date)
                }
                showCalendarDialog = false
            },
            onDismissRequest = { showCalendarDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeSelector(
    title: String,
    time: LocalDateTime?,
    lessonDate: LocalDate?,
    onTimeChange: (LocalDateTime?) -> Unit,
    modifier: Modifier = Modifier
) {
    var showTimeDialog by remember { mutableStateOf(false) }

    // Для диалога времени
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    if (lessonDate != null) {
                        showTimeDialog = true
                    }
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.i24_schedule),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = time?.formatTime() ?: stringResource(Res.string.not_set),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                )

                if (time != null) {
                    Text(
                        text = stringResource(Res.string.clear),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            onTimeChange(null)
                        }
                    )
                }
            }
        }
    }

    // Диалог выбора времени
    if (showTimeDialog && lessonDate != null) {
        TimePickerDialog(
            initialTime = time ?: LocalDateTime(lessonDate, LocalTime(now.hour, now.minute)),
            onConfirm = { timePickerState ->
                val newDateTime = LocalDateTime(
                    year = lessonDate.year,
                    monthNumber = lessonDate.monthNumber,
                    dayOfMonth = lessonDate.dayOfMonth,
                    hour = timePickerState.hour,
                    minute = timePickerState.minute
                )
                onTimeChange(newDateTime)
                showTimeDialog = false
            },
            onDismiss = { showTimeDialog = false }
        )
    }
}
