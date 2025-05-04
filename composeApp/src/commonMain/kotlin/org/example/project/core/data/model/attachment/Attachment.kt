package org.example.project.core.data.model.attachment

sealed interface Attachment {
    val url: String

    data class ImageAttachment(override val url: String) : Attachment
    data class FileAttachment(override val url: String) : Attachment
}