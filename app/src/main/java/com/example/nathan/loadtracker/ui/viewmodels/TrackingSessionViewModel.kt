package com.example.nathan.loadtracker.ui.viewmodels

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import com.example.nathan.loadtracker.core.database.entities.Load
import com.example.nathan.loadtracker.core.repository.LoadTrackerRepository
import com.example.nathan.loadtracker.ui.datamodels.MainUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class TrackingSessionViewModel(context: Application, dataStore: DataStore<Preferences>) : ViewModel() {

    private val _repository = LoadTrackerRepository(context = context, dataStore = dataStore)
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

    val mainUiModel = _mainUiModelFlow

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

    val loadsForActiveJobSession: Flow<List<Load>>
        get() = _repository.getLoadsForActiveJobSession()

    class Factory(val context: Application, val dataStore: DataStore<Preferences>) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TrackingSessionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TrackingSessionViewModel(context = context, dataStore = dataStore) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}