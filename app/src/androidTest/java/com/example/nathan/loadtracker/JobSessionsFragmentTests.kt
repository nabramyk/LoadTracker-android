package com.example.nathan.loadtracker

import android.view.View
import android.widget.Button
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.nathan.loadtracker.core.repository.MockLoadTrackerRepository
import com.example.nathan.loadtracker.ui.arrayadapters.JobSessionAdapter
import com.example.nathan.loadtracker.ui.viewmodels.MainViewModel
import com.example.nathan.loadtracker.ui.views.JobSessionsFragment
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class JobSessionsFragmentTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        viewModel = MainViewModel(MockLoadTrackerRepository())
    }

    @Test
    fun testClosingAJobSessionDoesNotRedirect() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        launchFragmentInHiltContainer<JobSessionsFragment>(
            action = {
                navController.setGraph(R.navigation.nav_graph)
                Navigation.setViewNavController(this.requireView(), navController)
            }
        )

        navController.currentDestination?.id = R.id.jobSessionsFragment
        onView(withId(R.id.rvJobSessions))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<JobSessionAdapter.JobSessionViewHolder>(
                    0,
                    CustomAction()
                )
            )

        Thread.sleep(500)
        onView(withText("Nah")).perform(click())

        assertEquals(navController.currentDestination?.id, R.id.jobSessionsFragment)
    }

    inner class CustomAction: ViewAction {
        override fun getDescription(): String {
            return ""
        }

        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun perform(uiController: UiController, view: View) {
            view.findViewById<Button>(R.id.bClose).performClick()
        }
    }
}
