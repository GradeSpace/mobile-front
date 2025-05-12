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
import mobile_front.composeapp.generated.resources.i24_schedule
import org.example.project.features.lessons.domain.AttendanceStatus
import org.example.project.features.lessons.domain.LessonStatus
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
            text = "Занятие еще не началось",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            icon = vectorResource(Res.drawable.i24_schedule)
        )
        is LessonStatus.InProgress -> LessonStatusInfo(
            text = "Занятие идет прямо сейчас",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            icon = Icons.Default.PlayArrow
        )
        is LessonStatus.Finished -> LessonStatusInfo(
            text = "Занятие завершилось",
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
                is LessonStatus.NotStarted -> "Отметка о посещении недоступна"
                is LessonStatus.InProgress -> null
                is LessonStatus.Finished -> "Вы не посетили это занятие"
            },
            color = when (lessonStatus) {
                is LessonStatus.NotStarted -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                else -> MaterialTheme.colorScheme.error
            },
            style = MaterialTheme.typography.labelMedium,
            showButton = lessonStatus is LessonStatus.InProgress
        )
        is AttendanceStatus.AttendanceOnCheck -> AttendanceStatusInfo(
            text = "Вы отметились, ожидайте подтверждения",
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.labelMedium.copy(fontStyle = FontStyle.Italic),
            showButton = false
        )
        is AttendanceStatus.Attended -> AttendanceStatusInfo(
            text = "Вы посетили это занятие",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            showButton = false
        )
    }
}