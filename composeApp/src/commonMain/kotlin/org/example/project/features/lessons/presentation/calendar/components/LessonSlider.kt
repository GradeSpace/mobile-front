package org.example.project.features.lessons.presentation.calendar.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.i24_schedule
import mobile_front.composeapp.generated.resources.ic_no_lessons
import mobile_front.composeapp.generated.resources.no_lessons_for_today
import org.example.project.Platform
import org.example.project.core.domain.EventItemTimeFormat
import org.example.project.core.presentation.ui.common.EventListItem
import org.example.project.features.lessons.domain.AttendanceStatus
import org.example.project.features.lessons.domain.LessonEventItem
import org.example.project.features.lessons.domain.LessonStatus
import org.example.project.getPlatform
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun LessonSlider(
    dayPagerState: PagerState,
    onLessonClick: (LessonEventItem) -> Unit,
    selectedDayLessons: (page: Int) -> List<LessonEventItem>,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = dayPagerState,
        modifier = modifier
            .wrapContentSize(),
        verticalAlignment = Alignment.Top,
        userScrollEnabled = getPlatform() != Platform.Desktop
    ) { day ->
        val lessons = selectedDayLessons(day)
        if (lessons.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(top = 98.dp)
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_no_lessons),
                    contentDescription = null,
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(bottom = 8.dp)
                )
                Text(
                    text = stringResource(Res.string.no_lessons_for_today),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                lessons.forEach { lesson ->
                    EventListItem(
                        eventItem = lesson,
                        timeFormat = EventItemTimeFormat.Lesson(
                            lesson.endTime,
                            lesson.location
                        ),
                        onClick = { onLessonClick(lesson) },
                        modifier = Modifier
                            .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
                    ) {
                        // Определяем информацию о статусе занятия
                        val lessonStatusInfo = when (lesson.lessonStatus) {
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

                        // Определяем информацию о статусе посещения
                        val attendanceStatusInfo = when (lesson.attendanceStatus) {
                            is AttendanceStatus.NotAttended -> AttendanceStatusInfo(
                                text = when (lesson.lessonStatus) {
                                    is LessonStatus.NotStarted -> "Отметка о посещении недоступна"
                                    is LessonStatus.InProgress -> null
                                    is LessonStatus.Finished -> "Вы не посетили это занятие"
                                },
                                color = when (lesson.lessonStatus) {
                                    is LessonStatus.NotStarted -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                    else -> MaterialTheme.colorScheme.error
                                },
                                style = MaterialTheme.typography.labelMedium,
                                showButton = lesson.lessonStatus is LessonStatus.InProgress
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

                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Icon(
                                    imageVector = lessonStatusInfo.icon,
                                    contentDescription = null,
                                    tint = lessonStatusInfo.color,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
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
                                    modifier = Modifier.padding(start = 22.dp, top = 2.dp)
                                )
                            }

                            if (attendanceStatusInfo.showButton) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ),
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text("Отметиться")
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

private data class LessonStatusInfo(
    val text: String,
    val color: Color,
    val style: TextStyle,
    val icon: ImageVector
)

private data class AttendanceStatusInfo(
    val text: String? = null,
    val color: Color,
    val style: TextStyle,
    val showButton: Boolean
)
