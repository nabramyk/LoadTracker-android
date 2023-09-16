package com.example.nathan.loadtracker

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

class LoadTrackerApplication : Application() {

    companion object {
        val Context.dataStore by preferencesDataStore(
            name = "persistent_data_preferences"
        )
    }
}