package com.example.nathan.loadtracker.ui.viewmodels

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import com.example.nathan.loadtracker.core.repository.LoadTrackerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ExportViewModel(context: Application, dataStore: DataStore<Preferences>) : ViewModel() {
    private val _repository = LoadTrackerRepository(context = context, dataStore = dataStore)

    private val _startTime = MutableLiveData("")
    private var _endTime = MutableLiveData("")
    private var _startDate = MutableLiveData("")
    private var _endDate = MutableLiveData("")

    private var mutableJobSessionWithLoads = MutableLiveData<JobSessionWithLoads>()
    val allJobSessions: LiveData<List<JobSession>> = _repository.getAllJobSessions()

    init {
        viewModelScope.launch {
            selectJobSessionToExport(_repository.getAllJobSessions().value!!.first().id)
        }
    }

    val startTime: LiveData<String>
        get() = _startTime
    val endTime: LiveData<String>
        get() = _endTime
    val startDate: LiveData<String>
        get() = _startDate
    val endDate: LiveData<String>
        get() = _endDate

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

    fun now(): LocalDateTime {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun updateStartTime(newTime: String) = _startTime.postValue(newTime)
    fun updateEndTime(newTime: String) = _endTime.postValue(newTime)
    fun updateStartDate(newDate: String) = _startDate.postValue(newDate)
    fun updateEndDate(newDate: String) = _endDate.postValue(newDate)

    class Factory(val context: Application, val dataStore: DataStore<Preferences>) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExportViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ExportViewModel(context = context, dataStore = dataStore) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}