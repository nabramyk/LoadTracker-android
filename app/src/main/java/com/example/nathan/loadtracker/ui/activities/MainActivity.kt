package com.example.nathan.loadtracker.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.nathan.loadtracker.LoadTrackerApplication.Companion.dataStore
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.databinding.ActivityMainBinding
import com.example.nathan.loadtracker.databinding.CreateSessionDialogBinding
import com.example.nathan.loadtracker.ui.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var lDrawerToggle: ActionBarDrawerToggle
    private lateinit var navController: NavController

    private val viewModel: MainViewModel by viewModels {
        MainViewModel.Factory(
            context = application,
            dataStore = application.applicationContext.dataStore
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        // The viewModel won't be instantiated
        // before the fragments unless I do it explicitly
        // Why?! Why God!? Why!?
        // TODO: Fix this bullshit
        viewModel

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment

        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        title = ""

        lDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.nav_open,
            R.string.nav_close
        )
        binding.drawerLayout.addDrawerListener(lDrawerToggle)
        lDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.nvNavigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.new_job_session -> {
                    showCreateDialog(viewModel)
                    true
                }

                R.id.view_job_sessions -> {
                    Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.settings -> {
                    Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.export -> {
                    Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (lDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun showCreateDialog(viewModel: MainViewModel) {
        val dialogBinding = CreateSessionDialogBinding.inflate(layoutInflater)

        val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)

        val alertDialog = alertDialogBuilder
            .setView(dialogBinding.root)
            .setCancelable(false)
            .setPositiveButton("Create") { _, _ ->
                if (!TextUtils.isEmpty(dialogBinding.sessionTitleEditText.text)) {
                    viewModel.addJobSession(
                        jobTitle = dialogBinding.sessionTitleEditText.text.toString()
                    )
                    showStartImmediateDialog()
                }
            }
            .setNegativeButton(
                "Cancel"
            ) { dialog, _ -> dialog.cancel() }
            .create()

        alertDialog.show()

        dialogBinding.sessionTitleEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 0) {
                    dialogBinding.sessionTitleEditText.error = "Session title must not be empty"
                }
            }
        })
    }

    private fun showStartImmediateDialog() {
        AlertDialog
            .Builder(this@MainActivity)
            .setMessage("Start this session now?")
            .setPositiveButton("Yes") { _, _ ->

            }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }

    enum class Tab {
        TRACKING,
        STATS,
        HISTORY
    }
}