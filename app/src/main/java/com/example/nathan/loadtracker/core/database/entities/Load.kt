package com.example.nathan.loadtracker.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity
data class Load(
        @PrimaryKey(autoGenerate = true) var id: Long = 0,
        var driver: String = "",
        var unitId: String = "",
        var material: String = "",
        var timeLoaded: Instant,
        var created: Instant,
        var modified: Instant? = null,
        var companyName: String? = null,
        var jobSessionId: Long
)