package org.example.project.features.tasks.presentation.tasks_list

import org.example.project.features.tasks.domain.TaskEventItem
import org.example.project.features.tasks.domain.TasksEventsBlock

sealed interface TasksListAction {
    data class TasksListItemClick(val item: TaskEventItem) : TasksListAction
    data object CreateNewTask : TasksListAction
    data object ToggleFilterMenu : TasksListAction
    data class ToggleBlockTypeFilter(val blockType: TasksEventsBlock.BlockType) : TasksListAction
    data object OnPullToRefresh : TasksListAction
}