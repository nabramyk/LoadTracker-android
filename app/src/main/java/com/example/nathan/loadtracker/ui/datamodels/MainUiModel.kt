package com.example.nathan.loadtracker.ui.datamodels

import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads

data class MainUiModel(
    val driverName: String,
    val companyName: String,
    val activeJobSessionWithLoads: JobSessionWithLoads?
)