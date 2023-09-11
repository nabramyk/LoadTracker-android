package com.example.nathan.loadtracker.ui.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import com.example.nathan.loadtracker.core.repository.LoadTrackerRepository

class TrackingViewModel(application: Application): ViewModel() {

    private val loadTrackerRepository = LoadTrackerRepository(application)

    private val mutableJobSession = MutableLiveData<JobSessionWithLoads>()
    val selectedJobSession: LiveData<JobSessionWithLoads> get() = mutableJobSession

    fun selectJobSession(jobSession: JobSessionWithLoads) {
        mutableJobSession.value = jobSession
    }

    fun addLoad(driver: String,
                unitId: String,
                material: String,
                timeLoaded: String,
                dateLoaded: String,
                created: String?,
                companyName: String?) {
        loadTrackerRepository.addLoad(
            driver, unitId, material, timeLoaded, dateLoaded, created, companyName, selectedJobSession.value!!.jobSession.id
        )
    }
}