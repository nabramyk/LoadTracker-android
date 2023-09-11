package com.example.nathan.loadtracker.core.database.dao

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
    fun insert(jobSession: JobSession)

    @Query("SELECT * FROM jobsession WHERE id=:jobSessionId")
    fun getJobSessionById(jobSessionId: Long): JobSession

    @Query("SELECT * FROM jobsession")
    fun all(): List<JobSession>

    @Transaction
    @Query("SELECT * FROM jobsession")
    fun allJobSessionsWithLoads(): List<JobSessionWithLoads>

    @Transaction
    @Query("SELECT * FROM jobsession where id = :jobSessionId")
    fun getJobSessionWithLoads(jobSessionId: Long): JobSessionWithLoads

    @Insert(onConflict = OnConflictStrategy.NONE)
    fun addNewJobSession(jobSession: JobSession)
}