package com.example.nathan.loadtracker

import android.app.Application
import com.example.nathan.loadtracker.core.repository.LoadTrackerRepository

class LoadTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        LoadTrackerRepository(this)
    }
}