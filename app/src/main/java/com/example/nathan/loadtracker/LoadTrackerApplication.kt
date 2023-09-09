package com.example.nathan.loadtracker

import android.app.Application
import android.util.Log
import com.example.nathan.loadtracker.core.database.LoadTrackerDatabase
import io.objectbox.android.Admin

class LoadTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        LoadTrackerDatabase.init(this)
        if (BuildConfig.DEBUG) {
            val started = Admin(LoadTrackerDatabase.getBoxStore()).start(this)
            Log.i("ObjectBoxAdmin", "Started: $started")
        }
    }
}