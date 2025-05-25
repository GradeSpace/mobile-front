package org.example.project.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.example.project.core.presentation.ui.theme.GradeSpaceTheme
import org.example.project.features.tasks.data.repository.TasksMockRepository
import org.example.project.features.tasks.presentation.tasks_list.TasksListScreen
import org.example.project.features.tasks.presentation.tasks_list.TasksListState
import org.koin.compose.koinInject

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TasksPreview() {
    GradeSpaceTheme {
        TasksListScreen(
            state = TasksListState(
                isLoading = false,
                error = null,
                tasksBlocks = TasksMockRepository(koinInject(), koinInject()).localTasksBlocks
            ),
            onAction = {}
        )
    }
}