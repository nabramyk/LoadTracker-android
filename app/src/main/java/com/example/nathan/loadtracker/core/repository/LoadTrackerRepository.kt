package com.example.nathan.loadtracker.core.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.nathan.loadtracker.core.database.LoadTrackerDatabase
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import com.example.nathan.loadtracker.core.database.entities.Load
import java.text.SimpleDateFormat

class LoadTrackerRepository(application: Application) {
    private val db: LoadTrackerDatabase = LoadTrackerDatabase.getInstance(application.applicationContext)

    fun addLoad(driver: String,
                unitId: String,
                material: String,
                timeLoaded: String,
                dateLoaded: String,
                created: String?,
                companyName: String?,
                jobSessionId: Long
    ) {
        db.loadDao().add(
            Load(
                driver = driver,
                unitId = unitId,
                material = material,
                timeLoaded = SimpleDateFormat("HH:mm:ss.SSS").format(timeLoaded),
                dateLoaded = SimpleDateFormat("yyyy/MM/dd").format(dateLoaded),
                created = SimpleDateFormat("yyyy/MM/dd").format(created),
                modified = null,
                companyName = companyName,
                jobSessionId = jobSessionId
            )
        )
    }

    suspend fun addJobSession(jobTitle: String) {
        db.jobSessionDao().add(
            JobSession(jobTitle = jobTitle)
        )
    }

    fun getAllJobSessions(): LiveData<List<JobSessionWithLoads>> {
        return db.jobSessionDao().allJobSessionsWithLoads()
    }
}