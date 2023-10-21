package com.example.nathan.loadtracker

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.nathan.loadtracker.core.repository.MockClock
import com.example.nathan.loadtracker.core.repository.MockLoadTrackerRepository
import com.example.nathan.loadtracker.ui.viewmodels.MainViewModel
import com.example.nathan.loadtracker.ui.views.ExportFragment
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ExportFragmentTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        viewModel = MainViewModel(
            MockLoadTrackerRepository(),
            MockClock()
        )
    }

    @Test
    fun testSelectingTodayOnlyCheckBoxFillsInDateAndTimeFieldsCorrectly() {
        launchFragmentInHiltContainer<ExportFragment>()

        onView(withId( R.id.sStartDate)).check { view, _ ->
            (view as TextInputEditText).text?.isBlank()
        }
        onView(withId( R.id.sEndDate)).check { view, _ ->
            (view as TextInputEditText).text?.isBlank()
        }
        onView(withId( R.id.sStartTime)).check { view, _ ->
            (view as TextInputEditText).text?.isBlank()
        }
        onView(withId( R.id.sEndTime)).check { view, _ ->
            (view as TextInputEditText).text?.isBlank()
        }

        onView(withId(R.id.cbCurrentDate)).perform(click())

        onView(withId( R.id.sStartDate)).check { view, _ ->
            (view as TextInputEditText).text?.isNotBlank()
        }
        onView(withId( R.id.sEndDate)).check { view, _ ->
            (view as TextInputEditText).text?.isNotBlank()
        }
        onView(withId( R.id.sStartTime)).check { view, _ ->
            (view as TextInputEditText).text?.isNotBlank()
        }
        onView(withId( R.id.sEndTime)).check { view, _ ->
            (view as TextInputEditText).text?.isNotBlank()
        }
    }
}