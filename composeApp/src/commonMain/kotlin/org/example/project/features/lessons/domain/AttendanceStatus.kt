package org.example.project.features.lessons.domain

sealed interface AttendanceStatus {
    data object NotAttended : AttendanceStatus
    data object AttendanceOnCheck : AttendanceStatus
    data object Attended : AttendanceStatus
}