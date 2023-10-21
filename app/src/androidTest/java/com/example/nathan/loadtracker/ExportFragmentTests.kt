package com.example.nathan.loadtracker

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.nathan.loadtracker.core.repository.MockLoadTrackerRepository
import com.example.nathan.loadtracker.ui.viewmodels.MainViewModel
import com.example.nathan.loadtracker.ui.views.ExportFragment
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.datetime.Month
import kotlinx.datetime.number
import org.hamcrest.Matchers
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
        viewModel = MainViewModel(MockLoadTrackerRepository())
    }

    @Test
    fun testSelectingTodayOnlyCheckBoxFillsInDateAndTimeFieldsCorrectly() {
        launchFragmentInHiltContainer<ExportFragment>()

        onView(withId(R.id.sStartDate)).check { view, _ ->
            assertThat((view as TextInputEditText).text.toString(), Matchers.blankOrNullString())
        }
        onView(withId(R.id.sEndDate)).check { view, _ ->
            assertThat((view as TextInputEditText).text.toString(), Matchers.blankOrNullString())
        }
        onView(withId(R.id.sStartTime)).check { view, _ ->
            assertThat((view as TextInputEditText).text.toString(), Matchers.blankOrNullString())
        }
        onView(withId(R.id.sEndTime)).check { view, _ ->
            assertThat((view as TextInputEditText).text.toString(), Matchers.blankOrNullString())
        }

        onView(withId(R.id.cbCurrentDate)).perform(click())

        val expectedStartDate = viewModel.now().run {
            "${Month.values()[this.month.number - 1]} ${this.dayOfMonth}, ${this.year}"
        }

        val expectedEndDate = viewModel.now().run {
            "${Month.values()[this.month.number - 1]} ${this.dayOfMonth}, ${this.year}"
        }

        val expectedStartTime = viewModel.now().run {
            // TODO Fix this dummy value
            "00:00 A.M."
        }

        val expectedEndTime = viewModel.now().run {
            // TODO Fix this dummy value
            "11:59 P.M."
        }

        onView(withId(R.id.sStartDate)).check { view, _ ->
            assertThat((view as TextInputEditText).text, Matchers.hasToString(expectedStartDate))
        }
        onView(withId(R.id.sEndDate)).check { view, _ ->
            assertThat((view as TextInputEditText).text, Matchers.hasToString(expectedEndDate))
        }
        onView(withId(R.id.sStartTime)).check { view, _ ->
            assertThat((view as TextInputEditText).text, Matchers.hasToString(expectedStartTime))
        }
        onView(withId(R.id.sEndTime)).check { view, _ ->
            assertThat((view as TextInputEditText).text, Matchers.hasToString(expectedEndTime))
        }
    }
}