package org.example.project.features.tasks.presentation.task_create

import kotlinx.datetime.LocalDateTime
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.note.GradeRange
import org.example.project.core.presentation.UiText
import org.example.project.features.tasks.domain.TaskVariant
import org.example.project.features.tasks.domain.VariantDistributionMode

data class TaskCreateState(
    val title: String = "",
    val titleError: UiText? = null,

    val description: String = "",
    val descriptionError: UiText? = null,

    val attachments: List<Attachment> = emptyList(),
    val pickedReceivers: List<String> = emptyList(),
    val availableReceivers: List<String> = emptyList(),

    val gradeRange: GradeRange? = null,
    val variantDistributionMode: VariantDistributionMode = VariantDistributionMode.NONE,
    val variants: List<TaskVariant>? = null,

    val deadline: LocalDateTime? = null,
    val issueTime: LocalDateTime? = null,

    val isBottomSheetAttachmentVisible: Boolean = false,
    val error: UiText? = null
)