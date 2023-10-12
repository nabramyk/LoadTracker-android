package com.example.nathan.loadtracker.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity
data class JobSession(
        @PrimaryKey(autoGenerate = true) var id: Long = 0,
        var jobTitle: String? = null,
        var startDate: Instant? = null,
        var closedDate: Instant? = null,
        var created: Instant
)
