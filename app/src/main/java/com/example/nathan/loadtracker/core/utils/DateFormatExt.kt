package com.example.nathan.loadtracker.core.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalTime
import java.time.format.DateTimeFormatter

fun LocalTime.format(pattern: String): String {
    return this.toJavaLocalTime().format(
        DateTimeFormatter.ofPattern(pattern)
    )
}

fun LocalDate.format(pattern: String): String {
    return this.toJavaLocalDate().format(
        DateTimeFormatter.ofPattern(pattern)
    )
}