package org.example.project.core.data.model.attachment

import kotlinx.serialization.Serializable

@Serializable
sealed interface Attachment {
    val id: String
    val url: String
    val fileName: String
    val fileSize: Long

    @Serializable
    data class FileAttachment(
        override val id: String,
        override val url: String,
        override val fileName: String,
        override val fileSize: Long,
        val fileType: FileType = FileType.UNKNOWN
    ) : Attachment

    @Serializable
    enum class FileType {
        IMAGE, PDF, WORD, UNKNOWN
    }
}

fun String.toFileType(): Attachment.FileType {
    return when(this) {
        in setOf("png", "jpg", "jpeg") -> Attachment.FileType.IMAGE
        "pdf" -> Attachment.FileType.PDF
        "word" -> Attachment.FileType.WORD
        else -> Attachment.FileType.UNKNOWN
    }
}