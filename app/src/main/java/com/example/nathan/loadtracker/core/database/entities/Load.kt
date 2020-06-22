package com.example.nathan.loadtracker.core.database.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class Load(
        @Id var id: Long = 0,
        var driver: String,
        var unitId: String,
        var material: String,
        var timeLoaded: String,
        var dateLoaded: String,
        var created: String?,
        var modified: String? = null,
        var companyName: String?
) {
    lateinit var jobSession: ToOne<JobSession>
}