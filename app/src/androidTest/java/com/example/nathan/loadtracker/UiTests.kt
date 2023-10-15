package com.example.nathan.loadtracker

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.nathan.loadtracker.core.repository.MockLoadTrackerRepository
import com.example.nathan.loadtracker.ui.viewmodels.MainViewModel
import com.example.nathan.loadtracker.ui.views.JobSessionsFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class UiTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        viewModel = MainViewModel(MockLoadTrackerRepository())
    }

    @Test
    fun testClosingAJobSession() {
        val fragment = launchFragmentInHiltContainer<JobSessionsFragment>()
    }
}