package com.example.nathan.loadtracker.ui.viewmodels

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import com.example.nathan.loadtracker.core.repository.LoadTrackerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.util.Calendar

class MainViewModel(context: Application, dataStore: DataStore<Preferences>) : ViewModel() {

    private val _repository = LoadTrackerRepository(context = context, dataStore = dataStore)
    private val _mutableJobSession: Flow<JobSessionWithLoads?> =
        _repository.activeJobSessionWithLoads
    private val _allJobSessions: Flow<List<JobSession>> = _repository.getAllJobSessions()

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

    private val _initializeAppModelFlow = combine(
        _repository.preferencesFlow,
        _allJobSessions,
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
        val c = Calendar.getInstance()
        viewModelScope.launch(Dispatchers.IO) {
            val jobSessionId = _mutableJobSession.first()?.jobSession?.id
            _repository.addLoad(
                driver,
                unitId,
                material,
                c.time,
                companyName,
                jobSessionId!!
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

    private val mutableJobSessions = _repository.getAllJobSessions()
    val allJobSessions: Flow<List<JobSession>> get() = mutableJobSessions

    private val mutableJobSessionWithLoads = MutableLiveData<JobSessionWithLoads>()

    val jobSessionToExport: LiveData<JobSessionWithLoads> get() = mutableJobSessionWithLoads

    fun selectJobSessionToExport(jobSessionId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            mutableJobSessionWithLoads.postValue(
                _repository.getJobSessionById(
                    jobSessionId
                )
            )
        }
    }

    fun selectJobSession(jobSessionId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.selectJobSession(jobSessionId)
        }
    }

    suspend fun deleteJobSession(jobSession: JobSession) {
        _repository.deleteJobSession(jobSession)
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