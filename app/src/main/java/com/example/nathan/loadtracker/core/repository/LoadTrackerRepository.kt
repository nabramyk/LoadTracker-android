package com.example.nathan.loadtracker.core.repository

import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import com.example.nathan.loadtracker.core.database.entities.Load
import kotlinx.coroutines.flow.Flow

interface LoadTrackerRepository {

    val preferencesFlow: Flow<DefaultLoadTrackerRepository.LoadTrackerPreferences>
    val activeJobSessionWithLoads: Flow<JobSessionWithLoads?>

    suspend fun addLoad(
        driver: String,
        unitId: String,
        material: String,
        companyName: String?
    )

    suspend fun addJobSession(jobTitle: String): Long

    suspend fun getLoadsForActiveJobSession(): Flow<List<Load>>

    fun getAllJobSessions(): Flow<List<JobSession>>

    suspend fun getJobSessionById(id: Long): JobSessionWithLoads

    suspend fun selectJobSession(jobSessionId: Long): JobSessionWithLoads

    suspend fun deleteJobSession(jobSession: JobSession)
}