package com.example.nathan.loadtracker.ui.viewmodels

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nathan.loadtracker.core.repository.LoadTrackerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewJobSessionViewModel(context: Application, dataStore: DataStore<Preferences>) : ViewModel() {

    private val _repository = LoadTrackerRepository(context = context, dataStore = dataStore)

    fun addJobSession(jobTitle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.addJobSession(
                jobTitle = jobTitle
            )
        }
    }

    class Factory(val context: Application, val dataStore: DataStore<Preferences>) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewJobSessionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NewJobSessionViewModel(context = context, dataStore = dataStore) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}