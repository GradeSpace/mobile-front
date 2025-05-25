package org.example.project.core.data.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime

object LocalDateTimeConverter {

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime): String {
        return dateTime.toString()
    }

    @TypeConverter
    fun toLocalDateTime(value: String): LocalDateTime {
        return LocalDateTime.parse(value)
    }
}
