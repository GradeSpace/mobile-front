package org.example.project.core.data.model.attachment

sealed interface Attachment {
    val url: String
    val fileName: String
    val fileSize: Long

    data class FileAttachment(
        override val url: String,
        override val fileName: String,
        override val fileSize: Long,
        val fileType: FileType = FileType.UNKNOWN
    ) : Attachment

    enum class FileType {
        IMAGE, PDF, WORD, UNKNOWN
    }
}