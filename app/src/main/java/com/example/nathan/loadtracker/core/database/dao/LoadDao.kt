package com.example.nathan.loadtracker.core.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.nathan.loadtracker.core.database.groups.LoadMaterialAmalgamation
import com.example.nathan.loadtracker.core.database.entities.Load

@Dao
interface LoadDao {
    @Insert
    suspend fun add(load: Load)

    @Query("SELECT * FROM load WHERE jobSessionId = :jobSessionId")
    suspend fun getLoadsForJobSession(jobSessionId: Long): List<Load>

    @Query("SELECT material, COUNT(*) AS count FROM load WHERE jobSessionId = :jobSessionId GROUP BY material")
    suspend fun getMaterialsSummaryFromLoadsForJobSession(jobSessionId: Long): List<LoadMaterialAmalgamation>
}