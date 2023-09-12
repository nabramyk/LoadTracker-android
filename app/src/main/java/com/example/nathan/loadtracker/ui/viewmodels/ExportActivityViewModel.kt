package com.example.nathan.loadtracker.ui.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import com.example.nathan.loadtracker.core.repository.LoadTrackerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class ExportActivityViewModel(application: Application): ViewModel() {

    private val loadTrackerRepository = LoadTrackerRepository(application)

    private val mutableJobSessions = loadTrackerRepository.getAllJobSessions()
    val allJobSessions: LiveData<List<JobSession>> get() = mutableJobSessions

    private val mutableJobSessionWithLoads = MutableLiveData<JobSessionWithLoads>()

    val jobSessionToExport: LiveData<JobSessionWithLoads> get() = mutableJobSessionWithLoads

    fun selectJobSessionToExport(jobSessionId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            mutableJobSessionWithLoads.postValue(loadTrackerRepository.getJobSessionById(jobSessionId))
        }
    }

    class Factory(val app: Application): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExportActivityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ExportActivityViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}