package com.example.nathan.loadtracker.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import com.example.nathan.loadtracker.core.database.entities.Load

@Dao
interface JobSessionDao {
    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun add(jobSession: JobSession): Long

    @Transaction
    @Query("SELECT * FROM jobsession WHERE id=:jobSessionId")
    suspend fun getJobSessionById(jobSessionId: Long): JobSessionWithLoads

    @Transaction
    @Query("SELECT * FROM jobsession")
    suspend fun allJobSessions(): List<JobSession>

    @Transaction
    @Query("SELECT * FROM jobsession where id = :jobSessionId")
    suspend fun getJobSessionWithLoads(jobSessionId: Long?): JobSessionWithLoads

    @Delete
    suspend fun deleteJobSessionAndLoads(jobSession: JobSession, loads: List<Load>)
}