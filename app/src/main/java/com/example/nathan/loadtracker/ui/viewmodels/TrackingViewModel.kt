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

class TrackingViewModel(application: Application): ViewModel() {

    private val loadTrackerRepository = LoadTrackerRepository(application)

    private val mutableJobSession = MutableLiveData<JobSessionWithLoads>()
    val selectedJobSession: LiveData<JobSessionWithLoads> get() = mutableJobSession

    private val mutableJobSessions = loadTrackerRepository.getAllJobSessions()

    val allJobSessionsWithLoads: LiveData<List<JobSessionWithLoads>> get() = mutableJobSessions

    val jobSessionToExport: JobSessionWithLoads? = null

    fun selectJobSession(jobSession: JobSessionWithLoads) {
        mutableJobSession.value = jobSession
    }

    fun addJobSession(jobTitle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadTrackerRepository.addJobSession(
                jobTitle = jobTitle
            )
        }
    }

    fun addLoad(driver: String,
                unitId: String,
                material: String,
                timeLoaded: String,
                dateLoaded: String,
                created: String?,
                companyName: String?) {
        loadTrackerRepository.addLoad(
            driver,
            unitId,
            material,
            timeLoaded,
            dateLoaded,
            created,
            companyName,
            selectedJobSession.value!!.jobSession.id
        )
    }

    class Factory(val app: Application): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TrackingViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TrackingViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}