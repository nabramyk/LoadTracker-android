package com.example.nathan.loadtracker.core.database

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class Converters {
    @TypeConverter
    fun fromInstant(value: String?) : Instant? {
        return value?.let { Instant.parse(value) }
    }

    @TypeConverter
    fun toInstant(instant: Instant?) : String? {
        return instant?.toString()
    }
}