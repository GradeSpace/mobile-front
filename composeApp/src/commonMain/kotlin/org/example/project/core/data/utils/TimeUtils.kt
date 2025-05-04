package org.example.project.core.data.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char

fun LocalDateTime.formatTime(withSeconds: Boolean = false): String {
    return this.format(
        LocalDateTime.Format {
            hour()
            char(':')
            minute()
            if (withSeconds) {
                char(':')
                second()
            }
        }
    )
}

fun LocalDateTime.formatDate(withYear: Boolean = false): String {
    return this.format(
        LocalDateTime.Format {
            dayOfMonth()
            char('/')
            monthNumber()
            if (withYear) {
                char('/')
                year()
            }
        }
    )
}