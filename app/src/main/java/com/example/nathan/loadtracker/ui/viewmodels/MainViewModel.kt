package com.example.nathan.loadtracker.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import com.example.nathan.loadtracker.core.repository.LoadTrackerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.util.Calendar

class MainViewModel(application: Application) : ViewModel() {
    private val _repository = LoadTrackerRepository(application)
    private val _preferences = application.getSharedPreferences(
        "com.example.nathan.loadtracker",
        Context.MODE_PRIVATE
    )

    private val _mutableJobSession: MutableLiveData<JobSessionWithLoads> = MutableLiveData()

    val selectedJobSession: LiveData<JobSessionWithLoads> get() = _mutableJobSession
    val persistentDriverName: String = _preferences.getString("name", "") ?: ""
    val persistentCompanyName: String = _preferences.getString("company", "") ?: ""

    init {
        viewModelScope.launch {
            _mutableJobSession.value =
                _repository
                    .getJobSessionById(
                        _preferences
                            .getLong(
                                "lastSelectedJobSessionId",
                                0L
                            )
                    )
        }
    }

    /**
     * Retrieve the selected job session from the db, and store the id in the shared preferences
     * so that the session can be automatically restored the next time the app is opened
     */
    fun selectJobSession(jobSessionId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _mutableJobSession
                .postValue(
                    _repository
                        .getJobSessionById(jobSessionId)
                ).also {
                    _preferences.edit().putLong("lastSelectedJobSessionId", jobSessionId)
                }
        }
    }

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
                selectedJobSession.value!!.jobSession.id
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

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}