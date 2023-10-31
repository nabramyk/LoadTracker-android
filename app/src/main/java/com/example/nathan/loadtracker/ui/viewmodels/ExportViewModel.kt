package com.example.nathan.loadtracker.ui.viewmodels

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import com.example.nathan.loadtracker.core.repository.LoadTrackerRepository
import com.example.nathan.loadtracker.core.utils.LoadTrackerCSVExporter
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.todayIn
import java.io.File
import java.time.format.DateTimeFormatter

class ExportViewModel(val context: Application, dataStore: DataStore<Preferences>) : ViewModel() {
    private val _repository = LoadTrackerRepository(context = context, dataStore = dataStore)

    private val _startDate = MutableLiveData<LocalDate?>()
    private val _startTime = MutableLiveData<LocalTime?>()

    private var _endDate = MutableLiveData<LocalDate?>()
    private var _endTime = MutableLiveData<LocalTime?>()

    private var mutableJobSessionWithLoads: Long = 0
    val allJobSessions: LiveData<List<JobSession>> = _repository.getAllJobSessions()

    lateinit var file: File

    init {
        viewModelScope.launch {
            _repository.getAllJobSessions().value?.let {
                selectJobSessionToExport(it.first().id)
            }
        }
    }

    val startTime: LiveData<String?>
        get() {
            return _startTime.map {
                it?.toJavaLocalTime()?.format(
                    DateTimeFormatter.ofPattern("HH:mm")
                )
            }
        }

    val endTime: LiveData<String?>
        get() {
            return _endTime.map {
                it?.toJavaLocalTime()?.format(
                    DateTimeFormatter.ofPattern("HH:mm")
                )
            }
        }

    val startDate: LiveData<String?>
        get() {
            return _startDate.map {
                it?.toJavaLocalDate()?.format(
                    DateTimeFormatter.ofPattern("MMM. dd, yyyy")
                )
            }
        }

    val endDate: LiveData<String?>
        get() {
            return _endDate.map {
                it?.toJavaLocalDate()?.format(
                    DateTimeFormatter.ofPattern("MMM. dd, yyyy")
                )
            }
        }

    suspend fun export() {
        val startD = _startDate.value ?: return
        val endD = _endDate.value ?: return
        val startT = _startTime.value ?: return
        val endT = _endTime.value ?: return

        val start = LocalDateTime(date = startD, time = startT)
        val end = LocalDateTime(date = endD, time = endT)

        _repository.getJobSessionById(mutableJobSessionWithLoads).let { jswl ->
            val exportableJobSession = JobSessionWithLoads(
                jobSession = jswl.jobSession,
                loads = jswl.loads.filter {
                    start.toInstant(TimeZone.currentSystemDefault()) <= it.timeLoaded &&
                            it.timeLoaded < end.toInstant(TimeZone.currentSystemDefault())
                }
            )

            file = LoadTrackerCSVExporter.export(context, exportableJobSession)
        }
    }

    fun selectJobSessionToExport(jobSessionId: Long) {
        mutableJobSessionWithLoads = jobSessionId
        Log.d("this", mutableJobSessionWithLoads.toString())
    }

    fun autoFillTimeRange(autofill: Boolean) {
        if (autofill) {
            val todayStart = Clock.System.todayIn(TimeZone.currentSystemDefault())
                .atTime(hour = 0, minute = 0, second = 0, nanosecond = 0)
            val todayEnd = Clock.System.todayIn(TimeZone.currentSystemDefault())
                .atTime(hour = 23, minute = 59, second = 59, nanosecond = 0)

            _startDate.postValue(todayStart.date)
            _startTime.postValue(todayStart.time)
            _endDate.postValue(todayEnd.date)
            _endTime.postValue(todayEnd.time)
        } else {
            _startDate.postValue(null)
            _startTime.postValue(null)
            _endDate.postValue(null)
            _endTime.postValue(null)
        }
    }

    fun updateStartTime(time: LocalTime) {
        _startTime.postValue(time)
    }

    fun updateEndTime(time: LocalTime) {
        _endTime.postValue(time)
    }

    fun updateStartDate(date: LocalDate) {
        _startDate.postValue(date)
    }

    fun updateEndDate(date: LocalDate) {
        _endDate.postValue(date)
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