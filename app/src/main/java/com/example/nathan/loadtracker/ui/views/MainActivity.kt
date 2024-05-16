package com.example.nathan.loadtracker.ui.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nathan.loadtracker.LoadTrackerApplication.Companion.dataStore
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.databinding.ActivityMainBinding
import com.example.nathan.loadtracker.ui.datamodels.InitializeAppModel
import com.example.nathan.loadtracker.ui.viewmodels.InitAppViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var lDrawerToggle: ActionBarDrawerToggle
    private val _navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    private val viewModel: InitAppViewModel by viewModels {
        InitAppViewModel.Factory(
            context = application, dataStore = application.applicationContext.dataStore
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        lDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity, binding.drawerLayout, R.string.nav_open, R.string.nav_close
        )
        binding.drawerLayout.addDrawerListener(lDrawerToggle)

        lifecycleScope.launch {
            viewModel.mainUiModel.collect { model ->
                if (model.activeJobSessionWithLoads != null) {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.initializeAppModel.first { model ->

                replaceNavigationGraph(model)
                binding.nvNavigationView.setupWithNavController(_navController)
                setupActionBarWithNavController(_navController, binding.drawerLayout)

                supportActionBar?.setDisplayHomeAsUpEnabled(true)

                binding.nvNavigationView.setNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.new_job_session -> {
                            _navController.navigate(R.id.action_trackingSessionFragment_to_newJobSessionFragment)
                            binding.drawerLayout.closeDrawer(GravityCompat.START)
                            true
                        }

                        R.id.view_job_sessions -> {
                            _navController.navigate(R.id.action_trackingSessionFragment_to_jobSessionsFragment)
                            binding.drawerLayout.closeDrawer(GravityCompat.START)
                            true
                        }

                        R.id.settings -> {
                            _navController.navigate(R.id.action_trackingSessionFragment_to_settingsFragment)
                            binding.drawerLayout.closeDrawer(GravityCompat.START)
                            true
                        }

                        R.id.export -> {
                            _navController.navigate(R.id.action_trackingSessionFragment_to_exportFragment)
                            binding.drawerLayout.closeDrawer(GravityCompat.START)
                            true
                        }

                        else -> false
                    }
                }

                true
            }
        }
    }

    fun replaceNavigationGraph(model: InitializeAppModel) {
        val graph = when {
            model.allJobSessions.not() -> R.navigation.nav_graph_new_session
            model.activeJobSession.not() -> R.navigation.nav_graph_pick_session
            else -> R.navigation.nav_graph
        }

        val navGraph = _navController.navInflater.inflate(graph)

        _navController.graph = navGraph
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // This is hacky and I don't much care for it. The navigation menu is treated as an options
        // menu, and when navigating from the home fragment to anywhere else it will override the
        // back button and the user will be stuck just repeatedly opening the navigation drawer.
        /** TODO Find a better way to intercept the back button when the user is farther down the fragment back stack */
        return if ((_navController.currentDestination?.id ?: 0) != R.id.trackingSessionFragment) {
            _navController.navigateUp()
        } else if (lDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    enum class Tab {
        TRACKING, STATS, HISTORY
    }
}