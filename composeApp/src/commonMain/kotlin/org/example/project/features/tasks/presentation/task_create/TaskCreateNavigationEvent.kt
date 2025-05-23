package org.example.project.features.tasks.presentation.task_create

import org.example.project.core.data.model.attachment.Attachment

sealed interface TaskCreateNavigationEvent {
    data object NavigateBack : TaskCreateNavigationEvent
    data class OpenFile(
        val attachment: Attachment
    ) : TaskCreateNavigationEvent
}