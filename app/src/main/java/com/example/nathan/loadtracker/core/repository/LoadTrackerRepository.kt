package com.example.nathan.loadtracker.core.repository

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.nathan.loadtracker.core.database.LoadTrackerDatabase
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import com.example.nathan.loadtracker.core.database.entities.Load
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class LoadTrackerRepository(
    context: Application,
    private val dataStore: DataStore<Preferences>
) {

    data class LoadTrackerPreferences(
        val selectedJobId: Long?,
        val driverName: String,
        val companyName: String
    )

    private object PreferencesKeys {
        val ACTIVE_JOB_SESSION_ID = longPreferencesKey("active_job_session_id")
        val DRIVER_NAME = stringPreferencesKey("driver_name")
        val COMPANY_NAME = stringPreferencesKey("company_name")
    }

    private val db: LoadTrackerDatabase =
        LoadTrackerDatabase.getInstance(context.applicationContext)

    val preferencesFlow: Flow<LoadTrackerPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            LoadTrackerPreferences(
                selectedJobId = preferences[PreferencesKeys.ACTIVE_JOB_SESSION_ID],
                driverName = preferences[PreferencesKeys.DRIVER_NAME] ?: "",
                companyName = preferences[PreferencesKeys.COMPANY_NAME] ?: ""
            )
        }

    val activeJobSessionWithLoads: Flow<JobSessionWithLoads?> = preferencesFlow
        .map {
            if (it.selectedJobId != null) {
                db.jobSessionDao().getJobSessionWithLoads(it.selectedJobId)
            } else {
                null
            }
        }

    suspend fun addLoad(
        driver: String,
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
        val newJobSessionId = db.jobSessionDao().add(
            JobSession(jobTitle = jobTitle)
        )
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACTIVE_JOB_SESSION_ID] = newJobSessionId
        }
        return newJobSessionId
    }

    suspend fun getLoadsForActiveJobSession(): Flow<List<Load>> {
        return flow {
            preferencesFlow.collect { preferences ->
                emit(db.loadDao().getLoadsForJobSession(preferences.selectedJobId ?: 0))
            }
        }
    }

    fun getAllJobSessions(): Flow<List<JobSession>> {
        return db.jobSessionDao().allJobSessions()
    }

    suspend fun getJobSessionById(id: Long): JobSessionWithLoads {
        return db.jobSessionDao().getJobSessionById(id)
    }

    /**
     * Retrieve the selected job session from the db, and store the id in the shared preferences
     * so that the session can be automatically restored the next time the app is opened
     */
    suspend fun selectJobSession(jobSessionId: Long): JobSessionWithLoads {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACTIVE_JOB_SESSION_ID] = jobSessionId
        }
        return getJobSessionById(jobSessionId)
    }

    suspend fun deleteJobSession(jobSession: JobSession) {
        if (activeJobSessionWithLoads.first()?.jobSession?.id == jobSession.id) {
            dataStore.edit { preferences ->
                preferences.remove(PreferencesKeys.ACTIVE_JOB_SESSION_ID)
            }
        }
        val loads = db.loadDao().getLoadsForJobSession(jobSessionId = jobSession.id)
        return db.jobSessionDao().deleteJobSessionAndLoads(jobSession, loads)
    }
}