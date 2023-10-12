package com.example.nathan.loadtracker.core.repository

import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import com.example.nathan.loadtracker.core.database.entities.Load
import kotlinx.coroutines.flow.Flow

class MockLoadTrackerRepository : LoadTrackerRepository {
    override suspend fun addLoad(
        driver: String,
        unitId: String,
        material: String,
        companyName: String?
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun addJobSession(jobTitle: String): Long {
        TODO("Not yet implemented")
    }

    override suspend fun getLoadsForActiveJobSession(): Flow<List<Load>> {
        TODO("Not yet implemented")
    }

    override fun getAllJobSessions(): Flow<List<JobSession>> {
        TODO("Not yet implemented")
    }

    override suspend fun getJobSessionById(id: Long): JobSessionWithLoads {
        TODO("Not yet implemented")
    }

    override suspend fun selectJobSession(jobSessionId: Long): JobSessionWithLoads {
        TODO("Not yet implemented")
    }

    override suspend fun deleteJobSession(jobSession: JobSession) {
        TODO("Not yet implemented")
    }
}