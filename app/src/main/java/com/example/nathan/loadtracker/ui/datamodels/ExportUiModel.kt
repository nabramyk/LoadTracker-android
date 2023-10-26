package com.example.nathan.loadtracker.ui.datamodels

import com.example.nathan.loadtracker.core.database.entities.JobSession

data class ExportUiModel(
    val startTime: String,
//    val endTime: String,
//    val startDate: String,
//    val endDate: String,
    val jobSessions: List<JobSession>
)