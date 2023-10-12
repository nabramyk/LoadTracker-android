package com.example.nathan.loadtracker

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.nathan.loadtracker.ui.views.JobSessionsFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UiTests {

    @Test
    fun testClosingAJobSession() {
        val scenario = launchFragmentInContainer<JobSessionsFragment>()

    }
}