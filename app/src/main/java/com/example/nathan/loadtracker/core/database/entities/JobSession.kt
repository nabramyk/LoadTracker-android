package com.example.nathan.loadtracker.core.database.entities

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
data class JobSession(
        @Id var id: Long = 0,
        var jobTitle: String? = null,
        var startDate: String? = null,
        var closedDate: String? = null,
        var created: String? = null,
        var totalLoads: Int? = 0
) {
    @Backlink
    lateinit var loads: ToMany<Load>
}
