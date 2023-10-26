package com.example.nathan.loadtracker.ui.viewmodels

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import com.example.nathan.loadtracker.core.database.entities.Load
import com.example.nathan.loadtracker.core.repository.LoadTrackerRepository
import com.example.nathan.loadtracker.ui.datamodels.MainUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(context: Application, dataStore: DataStore<Preferences>) : ViewModel() {

    private val _repository = LoadTrackerRepository(context = context, dataStore = dataStore)
    private val _mutableJobSession: Flow<JobSessionWithLoads?> =
        _repository.activeJobSessionWithLoads

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

    val allJobSessions: LiveData<List<JobSession>> = liveData { emit(_repository.getAllJobSessions()) }
    val mainUiModel = _mainUiModelFlow

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

    fun addLoad(
        driver: String,
        unitId: String,
        material: String,
        companyName: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.addLoad(
                driver,
                unitId,
                material,
                companyName
            )
        }
    }

    fun addJobSession(jobTitle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.addJobSession(
                jobTitle = jobTitle
            )
        }
    }

    suspend fun getLoadsForActiveJobSession(): Flow<List<Load>> {
        return _repository.getLoadsForActiveJobSession()
    }

    class Factory(val context: Application, val dataStore: DataStore<Preferences>) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(context = context, dataStore = dataStore) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}