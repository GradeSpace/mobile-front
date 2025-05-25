package org.example.project.core.data.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

object LocalDateConverter {

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.toString()
    }

    @TypeConverter
    fun toLocalDate(value: String): LocalDate {
        return LocalDate.parse(value)
    }
}