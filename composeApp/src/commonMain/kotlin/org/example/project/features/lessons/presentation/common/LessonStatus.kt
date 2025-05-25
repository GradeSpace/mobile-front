package org.example.project.features.lessons.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.checked_id_wait_for_verification
import mobile_front.composeapp.generated.resources.i24_schedule
import mobile_front.composeapp.generated.resources.lesson_ended
import mobile_front.composeapp.generated.resources.lesson_in_progress
import mobile_front.composeapp.generated.resources.lesson_missed
import mobile_front.composeapp.generated.resources.lesson_not_started
import mobile_front.composeapp.generated.resources.lesson_visited
import mobile_front.composeapp.generated.resources.no_checkin_allowed
import org.example.project.features.lessons.domain.AttendanceStatus
import org.example.project.features.lessons.domain.LessonStatus
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

data class LessonStatusInfo(
    val text: String,
    val color: Color,
    val style: TextStyle,
    val icon: ImageVector
)

data class AttendanceStatusInfo(
    val text: String? = null,
    val color: Color,
    val style: TextStyle,
    val showButton: Boolean
)

@Composable
fun LessonStatus.toStatusInfo(): LessonStatusInfo {
    return when (this) {
        is LessonStatus.NotStarted -> LessonStatusInfo(
            text = stringResource(Res.string.lesson_not_started),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            icon = vectorResource(Res.drawable.i24_schedule)
        )
        is LessonStatus.InProgress -> LessonStatusInfo(
            text = stringResource(Res.string.lesson_in_progress),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            icon = Icons.Default.PlayArrow
        )
        is LessonStatus.Finished -> LessonStatusInfo(
            text = stringResource(Res.string.lesson_ended),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            icon = Icons.Default.Done
        )
    }
}

@Composable
fun AttendanceStatus.toAttendanceInfo(lessonStatus: LessonStatus): AttendanceStatusInfo {
    return when(this) {
        is AttendanceStatus.NotAttended -> AttendanceStatusInfo(
            text = when (lessonStatus) {
                is LessonStatus.NotStarted -> stringResource(Res.string.no_checkin_allowed)
                is LessonStatus.InProgress -> null
                is LessonStatus.Finished -> stringResource(Res.string.lesson_missed)
            },
            color = when (lessonStatus) {
                is LessonStatus.NotStarted -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                else -> MaterialTheme.colorScheme.error
            },
            style = MaterialTheme.typography.labelMedium,
            showButton = lessonStatus is LessonStatus.InProgress
        )
        is AttendanceStatus.AttendanceOnCheck -> AttendanceStatusInfo(
            text = stringResource(Res.string.checked_id_wait_for_verification),
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.labelMedium.copy(fontStyle = FontStyle.Italic),
            showButton = false
        )
        is AttendanceStatus.Attended -> AttendanceStatusInfo(
            text = stringResource(Res.string.lesson_visited),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            showButton = false
        )
    }
}