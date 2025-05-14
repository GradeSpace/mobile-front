package org.example.project.features.tasks.presentation.task_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.accepted
import mobile_front.composeapp.generated.resources.issue_time_not_specified
import mobile_front.composeapp.generated.resources.issued
import mobile_front.composeapp.generated.resources.no_note
import mobile_front.composeapp.generated.resources.not_issued
import mobile_front.composeapp.generated.resources.not_specified
import mobile_front.composeapp.generated.resources.points
import mobile_front.composeapp.generated.resources.reason
import mobile_front.composeapp.generated.resources.rejected
import mobile_front.composeapp.generated.resources.since
import mobile_front.composeapp.generated.resources.under_check
import org.example.project.core.data.model.note.GradeRange
import org.example.project.core.data.utils.formatDateTime
import org.example.project.features.tasks.domain.TaskStatus
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskInfoDisplay(
    status: TaskStatus,
    deadline: LocalDateTime?,
    grade: GradeRange?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        // Status display
        when (status) {
            is TaskStatus.NotIssued -> NotIssuedStatusDisplay(status)
            is TaskStatus.Issued -> IssuedStatusDisplay(status)
            is TaskStatus.UnderCheck -> UnderCheckStatusDisplay(status)
            is TaskStatus.Rejected -> RejectedStatusDisplay(status)
            is TaskStatus.Completed -> CompletedStatusDisplay(status, grade)
        }

        // Deadline display for all statuses except Completed
        if (deadline != null && status !is TaskStatus.Completed) {
            TaskDeadlineDisplay(deadline)
        }
    }
}

@Composable
private fun NotIssuedStatusDisplay(status: TaskStatus.NotIssued) {
    StatusCard(
        title = stringResource(Res.string.not_issued),
        content = if (status.dateTime != null) {
            status.dateTime.formatDateTime()
        } else {
            stringResource(Res.string.issue_time_not_specified)
        },
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    )
}

@Composable
private fun IssuedStatusDisplay(status: TaskStatus.Issued) {
    StatusCard(
        title = stringResource(Res.string.issued),
        content = if (status.dateTime != null) {
            status.dateTime.formatDateTime()
        } else {
            stringResource(Res.string.issue_time_not_specified)
        },
        backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
    )
}

@Composable
private fun UnderCheckStatusDisplay(status: TaskStatus.UnderCheck) {
    StatusCard(
        title = stringResource(Res.string.under_check),
        content = if (status.dateTime != null) {
            "${stringResource(Res.string.since)} ${status.dateTime.formatDateTime()}"
        } else {
            stringResource(Res.string.issue_time_not_specified)
        },
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    )
}

@Composable
private fun RejectedStatusDisplay(status: TaskStatus.Rejected) {
    StatusCard(
        title = stringResource(Res.string.rejected),
        content = if (status.reason.isNullOrBlank()) {
            if (status.dateTime != null) {
                status.dateTime.formatDateTime()
            } else {
                "${stringResource(Res.string.reason)} ${stringResource(Res.string.not_specified)}"
            }
        } else {
            "${stringResource(Res.string.reason)}: ${status.reason}"
        },
        backgroundColor = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
    )
}

@Composable
private fun CompletedStatusDisplay(status: TaskStatus.Completed, grade: GradeRange?) {
    StatusCard(
        title = stringResource(Res.string.accepted),
        content = if (grade != null) {
            "${stringResource(Res.string.points)}: ${grade.currentGrade}/${grade.maxGrade}"
        } else {
            if (status.dateTime != null) {
                status.dateTime.formatDateTime()
            } else {
                stringResource(Res.string.no_note)
            }
        },
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    )
}

@Composable
private fun StatusCard(
    title: String,
    content: String,
    backgroundColor: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color,
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = contentColor
                )

                if (content.isNotEmpty()) {
                    Text(
                        text = content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor
                    )
                }
            }
        }
    }
}
