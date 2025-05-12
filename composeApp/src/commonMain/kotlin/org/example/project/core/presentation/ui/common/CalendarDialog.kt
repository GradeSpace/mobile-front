package org.example.project.core.presentation.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDialog(
    date: LocalDate,
    startDate: LocalDate,
    endDate: LocalDate,
    onDateChange: (Long?) -> Unit,
    onDismissRequest: () -> Unit
) {
    val initSelDayInMills = date.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()

    val startDateMillis = startDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    val endDateMillis = endDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()

    val selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis in startDateMillis..endDateMillis
        }

        override fun isSelectableYear(year: Int): Boolean {
            return year in startDate.year..endDate.year
        }
    }

    val state = rememberDatePickerState(
        yearRange = (startDate.year .. endDate.year),
        initialSelectedDateMillis = initSelDayInMills,
        selectableDates = selectableDates
    )

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            IconButton(
                onClick = {
                    onDateChange(
                        state.selectedDateMillis
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                )
            }
        },
        dismissButton = {
            IconButton(
                onClick = onDismissRequest
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        },
    )
    {
        DatePicker(
            state = state,
        )
    }
}