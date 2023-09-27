package com.example.nathan.loadtracker.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import kotlinx.coroutines.flow.Flow

@Dao
interface JobSessionDao {
    @Insert
    suspend fun add(jobSession: JobSession): Long

    @Transaction
    @Query("SELECT * FROM jobsession WHERE id=:jobSessionId")
    suspend fun getJobSessionById(jobSessionId: Long): JobSessionWithLoads

    @Transaction
    @Query("SELECT * FROM jobsession")
    fun allJobSessions(): Flow<List<JobSession>>

    @Transaction
    @Query("SELECT * FROM jobsession where id = :jobSessionId")
    suspend fun getJobSessionWithLoads(jobSessionId: Long?): JobSessionWithLoads

    @Insert(onConflict = OnConflictStrategy.NONE)
    fun addNewJobSession(jobSession: JobSession)
}