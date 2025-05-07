package org.example.project.features.tasks.presentation.task_screen

sealed interface TaskScreenAction {
    data object OnBackClick : TaskScreenAction
    data object OnPullToRefresh : TaskScreenAction
}