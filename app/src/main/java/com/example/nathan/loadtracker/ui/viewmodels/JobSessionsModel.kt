package com.example.nathan.loadtracker.ui.viewmodels

import com.example.nathan.loadtracker.core.database.entities.JobSession

data class JobSessionsModel(
    val allJobSessions: List<JobSession>,
    val activeJobSessionId: Long?
)
