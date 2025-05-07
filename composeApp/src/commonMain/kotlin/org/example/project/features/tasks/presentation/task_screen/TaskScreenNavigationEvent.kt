package org.example.project.features.tasks.presentation.task_screen

sealed interface TaskScreenNavigationEvent {
    data object NavigateBack : TaskScreenNavigationEvent
}