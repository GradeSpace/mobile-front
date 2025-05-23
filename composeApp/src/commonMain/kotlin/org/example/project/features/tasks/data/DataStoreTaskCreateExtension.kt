package org.example.project.features.tasks.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.project.core.data.datastore.DataStorePreferences
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.core.data.model.note.GradeRange
import org.example.project.features.tasks.domain.TaskCreateDraft
import org.example.project.features.tasks.domain.TaskVariant
import org.example.project.features.tasks.domain.VariantDistributionMode

// Keys for task create draft data
private const val TASK_DRAFT_TITLE_KEY = "task_draft_title_key"
private const val TASK_DRAFT_DESCRIPTION_KEY = "task_draft_description_key"
private const val TASK_DRAFT_RECEIVERS_KEY = "task_draft_receivers_key"
private const val TASK_DRAFT_ATTACHMENTS_KEY = "task_draft_attachments_key"
private const val TASK_DRAFT_GRADE_RANGE_KEY = "task_draft_grade_range_key"
private const val TASK_DRAFT_VARIANT_DISTRIBUTION_MODE_KEY = "task_draft_variant_distribution_mode_key"
private const val TASK_DRAFT_VARIANTS_KEY = "task_draft_variants_key"

// Title
suspend fun DataStorePreferences.getTaskDraftTitle(): String? {
    return getString(TASK_DRAFT_TITLE_KEY, "")
}

suspend fun DataStorePreferences.saveTaskDraftTitle(title: String) {
    setString(TASK_DRAFT_TITLE_KEY, title)
}

fun DataStorePreferences.getTaskDraftTitleFlow(): Flow<String?> {
    return getStringFlow(TASK_DRAFT_TITLE_KEY, "")
}

// Description
suspend fun DataStorePreferences.getTaskDraftDescription(): String? {
    return getString(TASK_DRAFT_DESCRIPTION_KEY, "")
}

suspend fun DataStorePreferences.saveTaskDraftDescription(description: String) {
    setString(TASK_DRAFT_DESCRIPTION_KEY, description)
}

fun DataStorePreferences.getTaskDraftDescriptionFlow(): Flow<String?> {
    return getStringFlow(TASK_DRAFT_DESCRIPTION_KEY, "")
}

// Receivers
suspend fun DataStorePreferences.getTaskDraftReceivers(): List<String>? {
    val receiversJson = getString(TASK_DRAFT_RECEIVERS_KEY, "")
    return try {
        Json.decodeFromString(receiversJson)
    } catch (e: Exception) {
        null
    }
}

suspend fun DataStorePreferences.saveTaskDraftReceivers(receivers: List<String>) {
    val receiversJson = Json.encodeToString(receivers)
    setString(TASK_DRAFT_RECEIVERS_KEY, receiversJson)
}

fun DataStorePreferences.getTaskDraftReceiversFlow(): Flow<List<String>?> {
    return getStringFlow(TASK_DRAFT_RECEIVERS_KEY, "").map { json ->
        try {
            Json.decodeFromString<List<String>>(json)
        } catch (e: Exception) {
            null
        }
    }
}

// Attachments
suspend fun DataStorePreferences.getTaskDraftAttachments(): List<Attachment>? {
    val attachmentsJson = getString(TASK_DRAFT_ATTACHMENTS_KEY, "")
    return try {
        Json.decodeFromString(attachmentsJson)
    } catch (e: Exception) {
        null
    }
}

suspend fun DataStorePreferences.saveTaskDraftAttachments(attachments: List<Attachment>) {
    val attachmentsJson = Json.encodeToString(attachments)
    setString(TASK_DRAFT_ATTACHMENTS_KEY, attachmentsJson)
}

fun DataStorePreferences.getTaskDraftAttachmentsFlow(): Flow<List<Attachment>?> {
    return getStringFlow(TASK_DRAFT_ATTACHMENTS_KEY, "").map { json ->
        try {
            Json.decodeFromString<List<Attachment>>(json)
        } catch (e: Exception) {
            null
        }
    }
}

// Grade Range
suspend fun DataStorePreferences.getTaskDraftGradeRange(): GradeRange? {
    val gradeRangeJson = getString(TASK_DRAFT_GRADE_RANGE_KEY, "")
    return try {
        Json.decodeFromString(gradeRangeJson)
    } catch (e: Exception) {
        null
    }
}

suspend fun DataStorePreferences.saveTaskDraftGradeRange(gradeRange: GradeRange) {
    val gradeRangeJson = Json.encodeToString(gradeRange)
    setString(TASK_DRAFT_GRADE_RANGE_KEY, gradeRangeJson)
}

fun DataStorePreferences.getTaskDraftGradeRangeFlow(): Flow<GradeRange?> {
    return getStringFlow(TASK_DRAFT_GRADE_RANGE_KEY, "").map { json ->
        try {
            Json.decodeFromString<GradeRange>(json)
        } catch (e: Exception) {
            null
        }
    }
}

// Variant Distribution Mode
suspend fun DataStorePreferences.getTaskDraftVariantDistributionMode(): VariantDistributionMode {
    val modeIndex = getInt(TASK_DRAFT_VARIANT_DISTRIBUTION_MODE_KEY, VariantDistributionMode.NONE.index)
    return VariantDistributionMode.entries.find { it.index == modeIndex } ?: VariantDistributionMode.NONE
}

suspend fun DataStorePreferences.saveTaskDraftVariantDistributionMode(mode: VariantDistributionMode) {
    setInt(TASK_DRAFT_VARIANT_DISTRIBUTION_MODE_KEY, mode.index)
}

fun DataStorePreferences.getTaskDraftVariantDistributionModeFlow(): Flow<VariantDistributionMode> {
    return getIntFlow(TASK_DRAFT_VARIANT_DISTRIBUTION_MODE_KEY, VariantDistributionMode.NONE.index).map { modeIndex ->
        VariantDistributionMode.entries.find { it.index == modeIndex } ?: VariantDistributionMode.NONE
    }
}

// Variants
suspend fun DataStorePreferences.getTaskDraftVariants(): List<TaskVariant>? {
    val variantsJson = getString(TASK_DRAFT_VARIANTS_KEY, "")
    return try {
        Json.decodeFromString(variantsJson)
    } catch (e: Exception) {
        null
    }
}

suspend fun DataStorePreferences.saveTaskDraftVariants(variants: List<TaskVariant>) {
    val variantsJson = Json.encodeToString(variants)
    setString(TASK_DRAFT_VARIANTS_KEY, variantsJson)
}

fun DataStorePreferences.getTaskDraftVariantsFlow(): Flow<List<TaskVariant>?> {
    return getStringFlow(TASK_DRAFT_VARIANTS_KEY, "").map { json ->
        try {
            Json.decodeFromString<List<TaskVariant>>(json)
        } catch (e: Exception) {
            null
        }
    }
}

// Get complete TaskCreateDraft object
suspend fun DataStorePreferences.getTaskCreateDraft(): TaskCreateDraft {
    return TaskCreateDraft(
        title = getTaskDraftTitle(),
        description = getTaskDraftDescription(),
        receivers = getTaskDraftReceivers(),
        attachments = getTaskDraftAttachments(),
        gradeRange = getTaskDraftGradeRange(),
        variantDistributionMode = getTaskDraftVariantDistributionMode(),
        variants = getTaskDraftVariants()
    )
}

// Clear all task draft data
suspend fun DataStorePreferences.clearTaskDraft() {
    remove(TASK_DRAFT_TITLE_KEY)
    remove(TASK_DRAFT_DESCRIPTION_KEY)
    remove(TASK_DRAFT_RECEIVERS_KEY)
    remove(TASK_DRAFT_ATTACHMENTS_KEY)
    remove(TASK_DRAFT_GRADE_RANGE_KEY)
    remove(TASK_DRAFT_VARIANT_DISTRIBUTION_MODE_KEY)
    remove(TASK_DRAFT_VARIANTS_KEY)
}
