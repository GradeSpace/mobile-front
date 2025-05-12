package org.example.project.features.lessons.presentation.lesson.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.features.lessons.domain.AttendanceStatus
import org.example.project.features.lessons.domain.LessonStatus
import org.example.project.features.lessons.presentation.common.toAttendanceInfo
import org.example.project.features.lessons.presentation.common.toStatusInfo

@Composable
fun LessonStatusBlock(
    lessonStatus: LessonStatus,
    attendanceStatus: AttendanceStatus,
    onClick:() -> Unit,
    modifier: Modifier = Modifier
) {
    val lessonStatusInfo = lessonStatus.toStatusInfo()

    val attendanceStatusInfo = attendanceStatus.toAttendanceInfo(lessonStatus)


    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.8f),
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Icon(
                    imageVector = lessonStatusInfo.icon,
                    contentDescription = null,
                    tint = lessonStatusInfo.color,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = lessonStatusInfo.text,
                    style = lessonStatusInfo.style,
                    color = lessonStatusInfo.color,
                )
            }

            attendanceStatusInfo.text?.let {
                Text(
                    text = attendanceStatusInfo.text,
                    style = attendanceStatusInfo.style,
                    color = attendanceStatusInfo.color,
                    modifier = Modifier.padding(start = 24.dp)
                )
            }

            if (attendanceStatusInfo.showButton) {
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Text("Отметиться")
                }
            }
        }
    }
}