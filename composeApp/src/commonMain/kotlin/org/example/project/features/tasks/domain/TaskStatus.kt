package org.example.project.features.tasks.domain

import kotlinx.datetime.LocalDateTime

sealed interface TaskStatus {
    val dateTime: LocalDateTime?

    data class NotIssued(override val dateTime: LocalDateTime? = null) : TaskStatus
    data class Issued(override val dateTime: LocalDateTime? = null) : TaskStatus
    data class UnderCheck(override val dateTime: LocalDateTime? = null) : TaskStatus
    data class Rejected(
        override val dateTime: LocalDateTime? = null,
        val reason: String? = null
    ) : TaskStatus

    data class Completed(
        override val dateTime: LocalDateTime? = null,
    ) : TaskStatus
}