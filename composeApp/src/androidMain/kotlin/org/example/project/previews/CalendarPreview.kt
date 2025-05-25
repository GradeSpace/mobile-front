package org.example.project.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.example.project.core.presentation.ui.theme.GradeSpaceTheme
import org.example.project.features.lessons.data.repository.LessonMockRepository
import org.example.project.features.lessons.presentation.calendar.CalendarScreen
import org.example.project.features.lessons.presentation.calendar.CalendarScreenState
import org.koin.compose.koinInject

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CalendarPreview() {
    GradeSpaceTheme {
        CalendarScreen(
            state = CalendarScreenState(
                isRefreshing = false,
                lessonBlocks = LessonMockRepository(koinInject(), koinInject()).localLessonBlocks
            ),
            onAction = {}
        )
    }
}