package org.example.project.features.tasks.presentation.task_screen

import org.example.project.core.presentation.UiText
import org.example.project.features.tasks.domain.TaskEventItem

data class TaskScreenState(
    val isRefreshing: Boolean = true,
    var error: UiText? = null,
    val taskItem: TaskEventItem? = null
)