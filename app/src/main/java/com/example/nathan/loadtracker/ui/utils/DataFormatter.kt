package com.example.nathan.loadtracker.ui.utils

import com.example.nathan.loadtracker.core.database.entities.Load
import kotlin.time.Duration

class DataFormatter {
    companion object {
        fun calculateAverageRunTime(loads: List<Load>): Duration {
            var averageRunTime: Duration = Duration.ZERO

            val first = loads.drop(1)
            val second = loads.dropLast(1)

            if (first.isNotEmpty()) {
                averageRunTime = first.mapIndexed { index, load ->
                    load.timeLoaded - second[index].timeLoaded
                }.reduce { current, next ->
                    return@reduce (current + next) / 2
                }
            }

            return averageRunTime
        }
    }
}