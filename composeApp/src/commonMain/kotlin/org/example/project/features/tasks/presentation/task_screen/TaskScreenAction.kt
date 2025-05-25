package org.example.project.features.tasks.presentation.task_screen

sealed interface TaskScreenAction {
    data object OnBackClick : TaskScreenAction
    data object OnPullToRefresh : TaskScreenAction
    data class OpenAttachment(val url: String) : TaskScreenAction
}