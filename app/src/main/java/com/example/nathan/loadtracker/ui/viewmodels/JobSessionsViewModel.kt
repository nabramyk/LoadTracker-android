package com.example.nathan.loadtracker.ui.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.repository.LoadTrackerRepository
import java.lang.IllegalArgumentException

class JobSessionsViewModel(application: Application): ViewModel() {

    private val loadTrackerRepository = LoadTrackerRepository(application)

    private val mutableJobSessions = loadTrackerRepository.getAllJobSessions()

    val allJobSessions: LiveData<List<JobSession>> get() = mutableJobSessions

    class Factory(val app: Application): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(JobSessionsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return JobSessionsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}