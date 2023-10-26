package com.example.nathan.loadtracker.ui.viewmodels

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import com.example.nathan.loadtracker.core.repository.LoadTrackerRepository
import com.example.nathan.loadtracker.ui.datamodels.InitializeAppModel
import com.example.nathan.loadtracker.ui.datamodels.MainUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.lang.IllegalArgumentException

class InitAppViewModel(context: Application, dataStore: DataStore<Preferences>) : ViewModel() {
    private val _repository = LoadTrackerRepository(context = context, dataStore = dataStore)
    val allJobSessions: LiveData<List<JobSession>> = liveData { emit(_repository.getAllJobSessions()) }
    private val _mutableJobSession: Flow<JobSessionWithLoads?> =
        _repository.activeJobSessionWithLoads

    private val _initializeAppModelFlow = combine(
        _repository.preferencesFlow,
        allJobSessions.asFlow(),
    ) { preferences, allJobSessions ->
        return@combine InitializeAppModel(
            allJobSessions = allJobSessions.isNotEmpty(),
            activeJobSession = preferences.selectedJobId != null,
        )
    }
    val initializeAppModel = _initializeAppModelFlow

    private val _mainUiModelFlow = combine(
        _repository.preferencesFlow,
        _mutableJobSession
    ) { _, jobSession ->
        return@combine MainUiModel(
            driverName = "",
            companyName = "",
            activeJobSessionWithLoads = jobSession
        )
    }
    val mainUiModel = _mainUiModelFlow

    class Factory(val context: Application, val dataStore: DataStore<Preferences>) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InitAppViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return InitAppViewModel(context = context, dataStore = dataStore) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}