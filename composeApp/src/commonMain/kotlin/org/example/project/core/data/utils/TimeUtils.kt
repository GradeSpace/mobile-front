package org.example.project.core.data.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import mobile_front.composeapp.generated.resources.Res
import mobile_front.composeapp.generated.resources.april
import mobile_front.composeapp.generated.resources.august
import mobile_front.composeapp.generated.resources.december
import mobile_front.composeapp.generated.resources.february
import mobile_front.composeapp.generated.resources.january
import mobile_front.composeapp.generated.resources.july
import mobile_front.composeapp.generated.resources.june
import mobile_front.composeapp.generated.resources.march
import mobile_front.composeapp.generated.resources.may
import mobile_front.composeapp.generated.resources.november
import mobile_front.composeapp.generated.resources.october
import mobile_front.composeapp.generated.resources.september
import org.jetbrains.compose.resources.StringResource

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

fun LocalDateTime.formatDateTime(
    withYear: Boolean = false,
    withSeconds: Boolean = false
): String {
    return "${this  .formatDate(withYear)} • ${this.formatTime(withSeconds)}"
}

fun getMonthResourceName(month: Month): StringResource? {
    return when (month) {
        Month.JANUARY -> Res.string.january
        Month.FEBRUARY -> Res.string.february
        Month.MARCH -> Res.string.march
        Month.APRIL -> Res.string.april
        Month.MAY -> Res.string.may
        Month.JUNE -> Res.string.june
        Month.JULY -> Res.string.july
        Month.AUGUST -> Res.string.august
        Month.SEPTEMBER -> Res.string.september
        Month.OCTOBER -> Res.string.october
        Month.NOVEMBER -> Res.string.november
        Month.DECEMBER -> Res.string.december
        else -> null
    }
}