package com.example.nathan.loadtracker.models

class JobSession(var id: Int? = 0,
                 var jobTitle: String? = null,
                 var startDate: String? = null,
                 var closedDate: String? = null,
                 var created: String? = null,
                 var totalLoads: Int? = 0
) {
    override fun toString(): String {
        return "ID: $id, Job Title: $jobTitle, Created: $created, Closed: $closedDate"
    }
}
