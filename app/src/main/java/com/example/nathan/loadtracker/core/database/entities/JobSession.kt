package com.example.nathan.loadtracker.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class JobSession(
        @PrimaryKey(autoGenerate = true) var id: Long = 0,
        var jobTitle: String? = null,
        var startDate: String? = null,
        var closedDate: String? = null,
        var created: String? = null,
        var totalLoads: Int? = 0,
)
