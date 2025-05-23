package org.example.project.features.tasks.presentation.task_create.components

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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.deadline
import mobile_front.composeapp.generated.resources.issue_time
import mobile_front.composeapp.generated.resources.not_set
import mobile_front.composeapp.generated.resources.set_date_time
import org.example.project.core.data.utils.formatDateTime
import org.example.project.core.presentation.ui.common.CalendarDialog
import org.example.project.core.presentation.ui.common.TimePickerDialog
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskDateTimeSection(
    deadline: LocalDateTime?,
    issueTime: LocalDateTime?,
    onDeadlineChange: (LocalDateTime?) -> Unit,
    onIssueTimeChange: (LocalDateTime?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Дедлайн
        DateTimeSelector(
            title = stringResource(Res.string.deadline),
            dateTime = deadline,
            onDateTimeChange = onDeadlineChange,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Время выдачи
        DateTimeSelector(
            title = stringResource(Res.string.issue_time),
            dateTime = issueTime,
            onDateTimeChange = onIssueTimeChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateTimeSelector(
    title: String,
    dateTime: LocalDateTime?,
    onDateTimeChange: (LocalDateTime?) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCalendarDialog by remember { mutableStateOf(false) }
    var showTimeDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(dateTime?.date) }

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
                    text = dateTime?.formatDateTime() ?: stringResource(Res.string.not_set),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                )

                if (dateTime != null) {
                    Text(
                        text = stringResource(Res.string.set_date_time),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            onDateTimeChange(null)
                            selectedDate = null
                        }
                    )
                }
            }
        }
    }

    // Диалог выбора даты
    if (showCalendarDialog) {
        CalendarDialog(
            date = selectedDate ?: now.date,
            startDate = startDate,
            endDate = endDate,
            onDateChange = { epochMillis ->
                if (epochMillis != null) {
                    val localDateTime = Instant
                        .fromEpochMilliseconds(epochMillis)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                    selectedDate = localDateTime.date
                    showCalendarDialog = false
                    showTimeDialog = true
                } else {
                    showCalendarDialog = false
                }
            },
            onDismissRequest = { showCalendarDialog = false }
        )
    }

    // Диалог выбора времени
    if (showTimeDialog && selectedDate != null) {
        TimePickerDialog(
            initialTime = dateTime ?: now,
            onConfirm = { timePickerState ->
                val newDateTime = LocalDateTime(
                    year = selectedDate!!.year,
                    monthNumber = selectedDate!!.monthNumber,
                    dayOfMonth = selectedDate!!.dayOfMonth,
                    hour = timePickerState.hour,
                    minute = timePickerState.minute
                )
                onDateTimeChange(newDateTime)
                showTimeDialog = false
            },
            onDismiss = { showTimeDialog = false }
        )
    }
}
