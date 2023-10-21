package com.example.nathan.loadtracker.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import com.example.nathan.loadtracker.core.database.entities.Load
import com.example.nathan.loadtracker.core.repository.LoadTrackerRepository
import com.example.nathan.loadtracker.ui.datamodels.MainUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val _repository: LoadTrackerRepository,
    private val _clock: Clock
) : ViewModel() {

    private val _mutableJobSession: Flow<JobSessionWithLoads?> =
        _repository.activeJobSessionWithLoads

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

    private val _jobSessionModelFlow = combine(
        _repository.preferencesFlow,
        _repository.getAllJobSessions()
    ) { preferences, jobSessions ->
        return@combine JobSessionsModel(
            activeJobSessionId = preferences.selectedJobId,
            allJobSessions = jobSessions
        )
    }

    val allJobSessions: Flow<List<JobSession>> = _repository.getAllJobSessions()
    val mainUiModel = _mainUiModelFlow
    val jobSessionModelFlow = _jobSessionModelFlow

    fun now(): LocalDateTime {
        return _clock.now().toLocalDateTime(TimeZone.UTC)
    }

    private val _initializeAppModelFlow = combine(
        _repository.preferencesFlow,
        allJobSessions,
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
        viewModelScope.launch(Dispatchers.IO) {
            _repository.addLoad(
                driver,
                unitId,
                material,
                companyName
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

    fun deleteJobSession(jobSession: JobSession) = viewModelScope.launch {
        _repository.deleteJobSession(jobSession)
    }

    suspend fun getLoadsForActiveJobSession(): Flow<List<Load>> {
        return _repository.getLoadsForActiveJobSession()
    }
}