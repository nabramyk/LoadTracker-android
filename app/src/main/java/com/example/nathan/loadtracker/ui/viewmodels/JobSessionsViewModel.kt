package com.example.nathan.loadtracker.ui.viewmodels

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.repository.LoadTrackerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class JobSessionsViewModel(context: Application, dataStore: DataStore<Preferences>) : ViewModel() {

    private val _repository = LoadTrackerRepository(context = context, dataStore = dataStore)
    val allJobSessions: LiveData<List<JobSession>> = _repository.getAllJobSessions()

    private val _jobSessionModelFlow = combine(
        _repository.preferencesFlow,
        allJobSessions.asFlow(),
    ) { preferences, jobSessions ->
        return@combine JobSessionsModel(
            activeJobSessionId = preferences.selectedJobId,
            allJobSessions = jobSessions
        )
    }
    val jobSessionModelFlow = _jobSessionModelFlow

    fun selectJobSession(jobSessionId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.selectJobSession(jobSessionId)
        }
    }

    fun deleteJobSession(jobSession: JobSession) = viewModelScope.launch {
        _repository.deleteJobSession(jobSession)
    }

    class Factory(val context: Application, val dataStore: DataStore<Preferences>) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(JobSessionsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return JobSessionsViewModel(context = context, dataStore = dataStore) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}