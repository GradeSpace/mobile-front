package org.example.project.features.tasks.presentation.tasks_list

import org.example.project.core.presentation.UiText
import org.example.project.features.tasks.domain.TasksEventsBlock

data class TasksListState(
    val isLoading: Boolean = true,
    val error: UiText? = null,
    val isRefreshing: Boolean = false,
    val tasksBlocks: List<TasksEventsBlock> = emptyList(),
    val showFiltersMenu: Boolean = false,
    val enabledBlockTypes: Set<TasksEventsBlock.BlockType> = TasksEventsBlock.BlockType.entries.toSet()
)
