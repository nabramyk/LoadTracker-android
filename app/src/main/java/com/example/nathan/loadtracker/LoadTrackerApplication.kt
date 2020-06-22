package com.example.nathan.loadtracker

import android.app.Application
import com.example.nathan.loadtracker.core.database.LoadTrackerDatabase

class LoadTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        LoadTrackerDatabase.init(this)
    }
}