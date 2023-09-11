package com.example.nathan.loadtracker.core.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads

@Dao
interface JobSessionDao {
    @Insert
    suspend fun add(jobSession: JobSession)

    @Query("SELECT * FROM jobsession WHERE id=:jobSessionId")
    fun getJobSessionById(jobSessionId: Long): JobSession

    @Transaction
    @Query("SELECT * FROM jobsession")
    fun allJobSessionsWithLoads(): LiveData<List<JobSessionWithLoads>>

    @Transaction
    @Query("SELECT * FROM jobsession where id = :jobSessionId")
    fun getJobSessionWithLoads(jobSessionId: Long): JobSessionWithLoads

    @Insert(onConflict = OnConflictStrategy.NONE)
    fun addNewJobSession(jobSession: JobSession)
}