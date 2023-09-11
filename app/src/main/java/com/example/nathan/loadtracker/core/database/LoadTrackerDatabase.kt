package com.example.nathan.loadtracker.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nathan.loadtracker.core.database.dao.JobSessionDao
import com.example.nathan.loadtracker.core.database.dao.LoadDao
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.Load

@Database(entities = [JobSession::class, Load::class], exportSchema = false, version = 1)
abstract class LoadTrackerDatabase: RoomDatabase() {

    companion object {
        private var instance: LoadTrackerDatabase? = null

        fun getInstance(context: Context): LoadTrackerDatabase {
            if (instance == null) {
                synchronized(LoadTrackerDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LoadTrackerDatabase::class.java, "load-tracker-database"
                    ).build()
                }
            }
            return instance!!
        }
    }

    abstract fun loadDao(): LoadDao
    abstract fun jobSessionDao(): JobSessionDao
}