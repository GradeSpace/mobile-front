package org.example.project.core.domain

enum class NavigationError : Error {
    URL_NOT_SUPPORTED,
    CANNOT_OPEN_THE_FILE,
    FAILED_TO_OPEN_URL
}