package com.example.nathan.loadtracker.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Load(
        @PrimaryKey(autoGenerate = true) var id: Long = 0,
        var driver: String = "",
        var unitId: String = "",
        var material: String = "",
        var timeLoaded: String = "",
        var dateLoaded: String = "",
        var created: String? = null,
        var modified: String? = null,
        var companyName: String? = null,
        var jobSessionId: Long
)