package org.example.project.features.lessons.data.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.project.core.data.datastore.DataStorePreferences
import org.example.project.core.data.model.attachment.Attachment
import org.example.project.features.lessons.domain.LessonCreateDraft

// Keys for lesson draft data
private const val LESSON_DRAFT_TITLE_KEY = "lesson_draft_title_key"
private const val LESSON_DRAFT_DESCRIPTION_KEY = "lesson_draft_description_key"
private const val LESSON_DRAFT_SUBJECT_KEY = "lesson_draft_subject_key"
private const val LESSON_DRAFT_RECEIVERS_KEY = "lesson_draft_receivers_key"
private const val LESSON_DRAFT_ATTACHMENTS_KEY = "lesson_draft_attachments_key"
private const val LESSON_DRAFT_DATE_KEY = "lesson_draft_date_key"
private const val LESSON_DRAFT_START_TIME_KEY = "lesson_draft_start_time_key"
private const val LESSON_DRAFT_END_TIME_KEY = "lesson_draft_end_time_key"
private const val LESSON_DRAFT_ONLINE_LOCATION_ENABLED_KEY = "lesson_draft_online_location_enabled_key"
private const val LESSON_DRAFT_OFFLINE_LOCATION_ENABLED_KEY = "lesson_draft_offline_location_enabled_key"
private const val LESSON_DRAFT_ONLINE_LINK_KEY = "lesson_draft_online_link_key"
private const val LESSON_DRAFT_OFFLINE_PLACE_KEY = "lesson_draft_offline_place_key"

// Title
suspend fun DataStorePreferences.getLessonDraftTitle(): String? {
    return getString(LESSON_DRAFT_TITLE_KEY, "")
}

suspend fun DataStorePreferences.saveLessonDraftTitle(title: String) {
    setString(LESSON_DRAFT_TITLE_KEY, title)
}

fun DataStorePreferences.getLessonDraftTitleFlow(): Flow<String?> {
    return getStringFlow(LESSON_DRAFT_TITLE_KEY, "")
}

// Subject
suspend fun DataStorePreferences.getLessonDraftSubject(): String? {
    return getString(LESSON_DRAFT_SUBJECT_KEY, "")
}

suspend fun DataStorePreferences.saveLessonDraftSubject(subject: String) {
    setString(LESSON_DRAFT_SUBJECT_KEY, subject)
}

fun DataStorePreferences.getLessonDraftSubjectFlow(): Flow<String?> {
    return getStringFlow(LESSON_DRAFT_SUBJECT_KEY, "")
}

// Description
suspend fun DataStorePreferences.getLessonDraftDescription(): String? {
    return getString(LESSON_DRAFT_DESCRIPTION_KEY, "")
}

suspend fun DataStorePreferences.saveLessonDraftDescription(description: String) {
    setString(LESSON_DRAFT_DESCRIPTION_KEY, description)
}

fun DataStorePreferences.getLessonDraftDescriptionFlow(): Flow<String?> {
    return getStringFlow(LESSON_DRAFT_DESCRIPTION_KEY, "")
}

// Receivers
suspend fun DataStorePreferences.getLessonDraftReceivers(): List<String>? {
    val receiversJson = getString(LESSON_DRAFT_RECEIVERS_KEY, "")
    return try {
        Json.decodeFromString(receiversJson)
    } catch (e: Exception) {
        null
    }
}

suspend fun DataStorePreferences.saveLessonDraftReceivers(receivers: List<String>) {
    val receiversJson = Json.encodeToString(receivers)
    setString(LESSON_DRAFT_RECEIVERS_KEY, receiversJson)
}

fun DataStorePreferences.getLessonDraftReceiversFlow(): Flow<List<String>?> {
    return getStringFlow(LESSON_DRAFT_RECEIVERS_KEY, "").map { json ->
        try {
            Json.decodeFromString<List<String>>(json)
        } catch (e: Exception) {
            null
        }
    }
}

// Attachments
suspend fun DataStorePreferences.getLessonDraftAttachments(): List<Attachment>? {
    val attachmentsJson = getString(LESSON_DRAFT_ATTACHMENTS_KEY, "")
    return try {
        Json.decodeFromString(attachmentsJson)
    } catch (e: Exception) {
        null
    }
}

suspend fun DataStorePreferences.saveLessonDraftAttachments(attachments: List<Attachment>) {
    val attachmentsJson = Json.encodeToString(attachments)
    setString(LESSON_DRAFT_ATTACHMENTS_KEY, attachmentsJson)
}

fun DataStorePreferences.getLessonDraftAttachmentsFlow(): Flow<List<Attachment>?> {
    return getStringFlow(LESSON_DRAFT_ATTACHMENTS_KEY, "").map { json ->
        try {
            Json.decodeFromString<List<Attachment>>(json)
        } catch (e: Exception) {
            null
        }
    }
}

// Lesson Date
suspend fun DataStorePreferences.getLessonDraftDate(): LocalDate? {
    val dateJson = getString(LESSON_DRAFT_DATE_KEY, "") ?: return null
    return try {
        Json.decodeFromString(dateJson)
    } catch (e: Exception) {
        null
    }
}

suspend fun DataStorePreferences.saveLessonDraftDate(date: LocalDate?) {
    if (date == null) {
        remove(LESSON_DRAFT_DATE_KEY)
    } else {
        val dateJson = Json.encodeToString(date)
        setString(LESSON_DRAFT_DATE_KEY, dateJson)
    }
}

fun DataStorePreferences.getLessonDraftDateFlow(): Flow<LocalDate?> {
    return getStringFlow(LESSON_DRAFT_DATE_KEY, "").map { json ->
        try {
            Json.decodeFromString<LocalDate>(json)
        } catch (e: Exception) {
            null
        }
    }
}

// Start Time
suspend fun DataStorePreferences.getLessonDraftStartTime(): LocalDateTime? {
    val timeJson = getString(LESSON_DRAFT_START_TIME_KEY, "")
    return try {
        Json.decodeFromString(timeJson)
    } catch (e: Exception) {
        null
    }
}

suspend fun DataStorePreferences.saveLessonDraftStartTime(time: LocalDateTime?) {
    if (time == null) {
        remove(LESSON_DRAFT_START_TIME_KEY)
    } else {
        val timeJson = Json.encodeToString(time)
        setString(LESSON_DRAFT_START_TIME_KEY, timeJson)
    }
}

fun DataStorePreferences.getLessonDraftStartTimeFlow(): Flow<LocalDateTime?> {
    return getStringFlow(LESSON_DRAFT_START_TIME_KEY, "").map { json ->
        try {
            Json.decodeFromString<LocalDateTime>(json)
        } catch (e: Exception) {
            null
        }
    }
}

// End Time
suspend fun DataStorePreferences.getLessonDraftEndTime(): LocalDateTime? {
    val timeJson = getString(LESSON_DRAFT_END_TIME_KEY, "")
    return try {
        Json.decodeFromString(timeJson)
    } catch (e: Exception) {
        null
    }
}

suspend fun DataStorePreferences.saveLessonDraftEndTime(time: LocalDateTime?) {
    if (time == null) {
        remove(LESSON_DRAFT_END_TIME_KEY)
    } else {
        val timeJson = Json.encodeToString(time)
        setString(LESSON_DRAFT_END_TIME_KEY, timeJson)
    }
}

fun DataStorePreferences.getLessonDraftEndTimeFlow(): Flow<LocalDateTime?> {
    return getStringFlow(LESSON_DRAFT_END_TIME_KEY, "").map { json ->
        try {
            Json.decodeFromString<LocalDateTime>(json)
        } catch (e: Exception) {
            null
        }
    }
}

// Online Location Enabled
suspend fun DataStorePreferences.isLessonDraftOnlineLocationEnabled(): Boolean {
    return getBoolean(LESSON_DRAFT_ONLINE_LOCATION_ENABLED_KEY, false)
}

suspend fun DataStorePreferences.saveLessonDraftOnlineLocationEnabled(enabled: Boolean) {
    setBoolean(LESSON_DRAFT_ONLINE_LOCATION_ENABLED_KEY, enabled)
}

fun DataStorePreferences.getLessonDraftOnlineLocationEnabledFlow(): Flow<Boolean> {
    return getBooleanFlow(LESSON_DRAFT_ONLINE_LOCATION_ENABLED_KEY, false)
}

// Offline Location Enabled
suspend fun DataStorePreferences.isLessonDraftOfflineLocationEnabled(): Boolean {
    return getBoolean(LESSON_DRAFT_OFFLINE_LOCATION_ENABLED_KEY, false)
}

suspend fun DataStorePreferences.saveLessonDraftOfflineLocationEnabled(enabled: Boolean) {
    setBoolean(LESSON_DRAFT_OFFLINE_LOCATION_ENABLED_KEY, enabled)
}

fun DataStorePreferences.getLessonDraftOfflineLocationEnabledFlow(): Flow<Boolean> {
    return getBooleanFlow(LESSON_DRAFT_OFFLINE_LOCATION_ENABLED_KEY, false)
}

// Online Link
suspend fun DataStorePreferences.getLessonDraftOnlineLink(): String? {
    return getString(LESSON_DRAFT_ONLINE_LINK_KEY, "")
}

suspend fun DataStorePreferences.saveLessonDraftOnlineLink(link: String) {
    setString(LESSON_DRAFT_ONLINE_LINK_KEY, link)
}

fun DataStorePreferences.getLessonDraftOnlineLinkFlow(): Flow<String?> {
    return getStringFlow(LESSON_DRAFT_ONLINE_LINK_KEY, "")
}

// Offline Place
suspend fun DataStorePreferences.getLessonDraftOfflinePlace(): String? {
    return getString(LESSON_DRAFT_OFFLINE_PLACE_KEY, "")
}

suspend fun DataStorePreferences.saveLessonDraftOfflinePlace(place: String) {
    setString(LESSON_DRAFT_OFFLINE_PLACE_KEY, place)
}

fun DataStorePreferences.getLessonDraftOfflinePlaceFlow(): Flow<String?> {
    return getStringFlow(LESSON_DRAFT_OFFLINE_PLACE_KEY, "")
}

// Get complete LessonCreateDraft object
suspend fun DataStorePreferences.getLessonCreateDraft(): LessonCreateDraft {
    // Получаем данные о локации
    val isOnlineEnabled = isLessonDraftOnlineLocationEnabled()
    val isOfflineEnabled = isLessonDraftOfflineLocationEnabled()
    val onlineLink = getLessonDraftOnlineLink()
    val offlinePlace = getLessonDraftOfflinePlace()

    return LessonCreateDraft(
        title = getLessonDraftTitle(),
        subject = getLessonDraftSubject(),
        description = getLessonDraftDescription(),
        receivers = getLessonDraftReceivers(),
        attachments = getLessonDraftAttachments(),
        lessonDate = getLessonDraftDate(),
        startTime = getLessonDraftStartTime(),
        endTime = getLessonDraftEndTime(),
        isOnlineLocationEnabled = isOnlineEnabled,
        isOfflineLocationEnabled = isOfflineEnabled,
        onlineLink = onlineLink ?: "",
        offlinePlace = offlinePlace ?: "",
    )
}

// Clear all lesson draft data
suspend fun DataStorePreferences.clearLessonDraft() {
    remove(LESSON_DRAFT_TITLE_KEY)
    remove(LESSON_DRAFT_SUBJECT_KEY)
    remove(LESSON_DRAFT_DESCRIPTION_KEY)
    remove(LESSON_DRAFT_RECEIVERS_KEY)
    remove(LESSON_DRAFT_ATTACHMENTS_KEY)
    remove(LESSON_DRAFT_DATE_KEY)
    remove(LESSON_DRAFT_START_TIME_KEY)
    remove(LESSON_DRAFT_END_TIME_KEY)
    remove(LESSON_DRAFT_ONLINE_LOCATION_ENABLED_KEY)
    remove(LESSON_DRAFT_OFFLINE_LOCATION_ENABLED_KEY)
    remove(LESSON_DRAFT_ONLINE_LINK_KEY)
    remove(LESSON_DRAFT_OFFLINE_PLACE_KEY)
}
