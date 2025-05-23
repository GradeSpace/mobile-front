// New file: DataStoreNotificationDraftExtension.kt
package org.example.project.features.feed.data.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.project.core.data.datastore.DataStorePreferences
import org.example.project.core.data.model.attachment.Attachment

// Keys for notification draft data
private const val NOTIFICATION_DRAFT_TITLE_KEY = "notification_draft_title_key"
private const val NOTIFICATION_DRAFT_DESCRIPTION_KEY = "notification_draft_description_key"
private const val NOTIFICATION_DRAFT_RECEIVERS_KEY = "notification_draft_receivers_key"
private const val NOTIFICATION_DRAFT_ATTACHMENTS_KEY = "notification_draft_attachments_key"

// Title
internal suspend fun DataStorePreferences.getNotificationDraftTitle(): String {
    return getString(NOTIFICATION_DRAFT_TITLE_KEY, "")
}

internal suspend fun DataStorePreferences.saveNotificationDraftTitle(title: String) {
    setString(NOTIFICATION_DRAFT_TITLE_KEY, title)
}

internal fun DataStorePreferences.getNotificationDraftTitleFlow(): Flow<String> {
    return getStringFlow(NOTIFICATION_DRAFT_TITLE_KEY, "")
}

// Description
internal suspend fun DataStorePreferences.getNotificationDraftDescription(): String {
    return getString(NOTIFICATION_DRAFT_DESCRIPTION_KEY, "")
}

internal suspend fun DataStorePreferences.saveNotificationDraftDescription(description: String) {
    setString(NOTIFICATION_DRAFT_DESCRIPTION_KEY, description)
}

internal fun DataStorePreferences.getNotificationDraftDescriptionFlow(): Flow<String> {
    return getStringFlow(NOTIFICATION_DRAFT_DESCRIPTION_KEY, "")
}

// Receivers
internal suspend fun DataStorePreferences.getNotificationDraftReceivers(): List<String> {
    val receiversJson = getString(NOTIFICATION_DRAFT_RECEIVERS_KEY, "[]")
    return try {
        Json.decodeFromString(receiversJson)
    } catch (e: Exception) {
        emptyList()
    }
}

internal suspend fun DataStorePreferences.saveNotificationDraftReceivers(receivers: List<String>) {
    val receiversJson = Json.encodeToString(receivers)
    setString(NOTIFICATION_DRAFT_RECEIVERS_KEY, receiversJson)
}

internal fun DataStorePreferences.getNotificationDraftReceiversFlow(): Flow<List<String>> {
    return getStringFlow(NOTIFICATION_DRAFT_RECEIVERS_KEY, "[]").map { json ->
        try {
            Json.decodeFromString<List<String>>(json)
        } catch (e: Exception) {
            emptyList()
        }
    }
}

// Attachments
internal suspend fun DataStorePreferences.getNotificationDraftAttachments(): List<Attachment> {
    val attachmentsJson = getString(NOTIFICATION_DRAFT_ATTACHMENTS_KEY, "[]")
    return try {
        Json.decodeFromString(attachmentsJson)
    } catch (e: Exception) {
        emptyList()
    }
}

internal suspend fun DataStorePreferences.saveNotificationDraftAttachments(attachments: List<Attachment>) {
    val attachmentsJson = Json.encodeToString(attachments)
    setString(NOTIFICATION_DRAFT_ATTACHMENTS_KEY, attachmentsJson)
}

internal fun DataStorePreferences.getNotificationDraftAttachmentsFlow(): Flow<List<Attachment>> {
    return getStringFlow(NOTIFICATION_DRAFT_ATTACHMENTS_KEY, "[]").map { json ->
        try {
            Json.decodeFromString<List<Attachment>>(json)
        } catch (e: Exception) {
            emptyList()
        }
    }
}

// Clear all draft data
internal suspend fun DataStorePreferences.clearNotificationDraft() {
    remove(NOTIFICATION_DRAFT_TITLE_KEY)
    remove(NOTIFICATION_DRAFT_DESCRIPTION_KEY)
    remove(NOTIFICATION_DRAFT_RECEIVERS_KEY)
    remove(NOTIFICATION_DRAFT_ATTACHMENTS_KEY)
}
