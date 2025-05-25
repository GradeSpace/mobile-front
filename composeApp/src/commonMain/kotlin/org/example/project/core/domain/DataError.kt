package org.example.project.core.domain

sealed interface DataError: Error {
    enum class Remote: DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER,
        SERIALIZATION,
        NOT_FOUND,
        UNKNOWN
    }

    enum class Local: DataError {
        DISK_FULL,
        UNKNOWN
    }

    enum class Auth : DataError {
        USER_NOT_FOUND,
        INVALID_PASSWORD,
        EMAIL_EXISTS
    }
}