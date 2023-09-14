package com.example.nathan.loadtracker.ui.viewmodels

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import com.example.nathan.loadtracker.core.repository.LoadTrackerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.util.Calendar

class MainViewModel(context: Application, dataStore: DataStore<Preferences>) : ViewModel() {

    private val _repository = LoadTrackerRepository(context = context, dataStore = dataStore)
    private val _mutableJobSession: Flow<JobSessionWithLoads> = _repository.activeJobSessionWithLoads

    private val mainUiModelFlow = combine(
        _repository.preferencesFlow,
        _mutableJobSession
    ) { _, jobSession ->
        return@combine MainUiModel(
            driverName = "",
            companyName = "",
            activeJobSessionWithLoads = jobSession
        )
    }

    val mainUiModel = mainUiModelFlow.asLiveData()

    fun addLoad(
        driver: String,
        unitId: String,
        material: String,
        companyName: String?
    ) {
        val c = Calendar.getInstance()
        viewModelScope.launch(Dispatchers.IO) {
            _repository.addLoad(
                driver,
                unitId,
                material,
                c.time,
                companyName,
                _mutableJobSession.single().jobSession.id
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
    val allJobSessions: LiveData<List<JobSession>> get() = mutableJobSessions

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

    class Factory(val context: Application, val dataStore: DataStore<Preferences>) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(context = context, dataStore = dataStore) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}