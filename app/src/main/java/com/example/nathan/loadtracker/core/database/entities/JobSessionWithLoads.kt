package com.example.nathan.loadtracker.core.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class JobSessionWithLoads(
    @Embedded val jobSession: JobSession,
    @Relation(
        parentColumn = "id",
        entityColumn = "jobSessionId"
    )
    val loads: List<Load>
)
