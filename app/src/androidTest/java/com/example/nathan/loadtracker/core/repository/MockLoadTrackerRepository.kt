package com.example.nathan.loadtracker.core.repository

import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import com.example.nathan.loadtracker.core.database.entities.Load
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock

class MockLoadTrackerRepository : LoadTrackerRepository {
    override val preferencesFlow: Flow<DefaultLoadTrackerRepository.LoadTrackerPreferences>
        get() = flow {
            DefaultLoadTrackerRepository.LoadTrackerPreferences(
                companyName = "huey",
                driverName = "dewey",
                selectedJobId = 0
            )
        }
    override val activeJobSessionWithLoads: Flow<JobSessionWithLoads?>
        get() = flow {
            JobSessionWithLoads(
                JobSession(
                    id = 1,
                    created = Clock.System.now()
                ),
                listOf()
            )
        }

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
        return flow {
            emptyList<JobSession>()
        }
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