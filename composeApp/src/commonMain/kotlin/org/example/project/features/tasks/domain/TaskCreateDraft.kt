package org.example.project.features.tasks.domain

import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.note.GradeRange

data class TaskCreateDraft(
    val title: String? = null,
    val description: String? = null,
    val receivers: List<String>? = null,
    val attachments: List<Attachment>? = null,
    val gradeRange: GradeRange? = null,
    val variantDistributionMode: VariantDistributionMode = VariantDistributionMode.NONE,
    val variants: List<TaskVariant>? = null
)
