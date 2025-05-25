package org.example.project.features.tasks.presentation.task_create

import kotlinx.datetime.LocalDateTime
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.note.GradeRange
import org.example.project.core.presentation.AttachmentSource
import org.example.project.features.tasks.domain.TaskVariant
import org.example.project.features.tasks.domain.VariantDistributionMode

sealed interface TasksCreateAction {
    data object OnBackClick : TasksCreateAction
    data object OnSendClick : TasksCreateAction
    data class OnTitleChange(val title: String) : TasksCreateAction
    data class OnDescriptionChange(val description: String) : TasksCreateAction
    data class OnReceiverSelected(val receiver: String) : TasksCreateAction
    data class OnReceiverDeselected(val receiver: String) : TasksCreateAction
    data class OnSourceSelected(val source: AttachmentSource) : TasksCreateAction
    data class OnRemoveAttachment(val attachment: Attachment) : TasksCreateAction
    data class OnAttachmentClick(val attachment: Attachment) : TasksCreateAction
    data class OnGradeRangeChange(val gradeRange: GradeRange?) : TasksCreateAction
    data class OnDeadlineChange(val deadline: LocalDateTime?) : TasksCreateAction
    data class OnIssueTimeChange(val issueTime: LocalDateTime?) : TasksCreateAction
    data class OnVariantDistributionModeChange(val mode: VariantDistributionMode) : TasksCreateAction
    data class OnVariantsChange(val variants: List<TaskVariant>?) : TasksCreateAction

}