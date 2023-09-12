package com.example.nathan.loadtracker.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.nathan.loadtracker.core.database.entities.Load

@Dao
interface LoadDao {
    @Insert
    suspend fun add(load: Load)

    @Query("SELECT * FROM load WHERE jobSessionId = :jobSessionId")
    fun getLoadsForJobSession(jobSessionId: Long): List<Load>
}