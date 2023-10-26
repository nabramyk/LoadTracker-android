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
import com.example.nathan.loadtracker.ui.datamodels.ExportUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.lang.IllegalArgumentException

class ExportViewModel(context: Application, dataStore: DataStore<Preferences>) : ViewModel() {
    private val _repository = LoadTrackerRepository(context = context, dataStore = dataStore)

    private var _startTime: String = ""
    private var _endTime: String = ""
    private var _startDate: String = ""
    private var _endDate: String = ""

    private val startTime: Flow<String>
        get() = flow { _startTime }

//    private val _exportUiModelFlow = combine(
//        _repository.getAllJobSessions(),
//        startTime
//    ) { jobSessions, startTime ->
//        return@combine ExportUiModel(
//            startTime = startTime,
//            jobSessions = jobSessions
//        )
//    }

    val allJobSessions: LiveData<List<JobSession>> = liveData {
        emit(_repository.getAllJobSessions())
    }

    //val exportUiModel = _exportUiModelFlow

    val jobSessionToExport: LiveData<JobSessionWithLoads> get() = mutableJobSessionWithLoads
    private val mutableJobSessionWithLoads = MutableLiveData<JobSessionWithLoads>()

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

    fun updateStartTime(newTime: String) {
        _startTime = newTime

    }

    fun updateEndTime(newTime: String) {
        _endTime = newTime
    }

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