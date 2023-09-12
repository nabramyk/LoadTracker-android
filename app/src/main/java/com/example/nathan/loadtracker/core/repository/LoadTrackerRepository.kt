package com.example.nathan.loadtracker.core.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.nathan.loadtracker.core.database.LoadTrackerDatabase
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import com.example.nathan.loadtracker.core.database.entities.Load
import java.text.SimpleDateFormat
import java.util.Date

class LoadTrackerRepository(application: Application) {
    private val db: LoadTrackerDatabase = LoadTrackerDatabase.getInstance(application.applicationContext)

    suspend fun addLoad(driver: String,
                unitId: String,
                material: String,
                timestamp: Date,
                companyName: String?,
                jobSessionId: Long
    ) {
        db.loadDao().add(
            Load(
                driver = driver,
                unitId = unitId,
                material = material,
                timeLoaded = SimpleDateFormat("HH:mm:ss.SSS").format(timestamp),
                dateLoaded = SimpleDateFormat("yyyy/MM/dd").format(timestamp),
                created = SimpleDateFormat("yyyy/MM/dd").format(timestamp),
                modified = null,
                companyName = companyName,
                jobSessionId = jobSessionId
            )
        )
    }

    suspend fun addJobSession(jobTitle: String): Long {
        return db.jobSessionDao().add(
            JobSession(jobTitle = jobTitle)
        )
    }

    fun getAllJobSessions(): LiveData<List<JobSession>> {
        return db.jobSessionDao().allJobSessions()
    }

    suspend fun getJobSessionById(id: Long): JobSessionWithLoads {
        return db.jobSessionDao().getJobSessionById(id)
    }
}