package com.example.nathan.loadtracker.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.databinding.ActivityMainBinding
import com.example.nathan.loadtracker.databinding.CreateSessionDialogBinding
import com.example.nathan.loadtracker.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var vAdapter: TrackingPagerAdapter
    private lateinit var lDrawerToggle: ActionBarDrawerToggle

    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // The viewModel won't be instantiated before the fragments unless I do it explicitly
        // Why?! Why God!? Why!?
        // TODO: Fix this bullshit
        viewModel

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

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

        vAdapter = TrackingPagerAdapter(supportFragmentManager)
        binding.vPager.adapter = vAdapter
        binding.vPager.currentItem = Tab.TRACKING.ordinal

        binding.vPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                binding.bottomNavigation.selectedItemId = when (position) {
                    0 -> R.id.fragment_track
                    1 -> R.id.fragment_stats
                    else -> R.id.fragment_history
                }
            }
        })

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            vAdapter.notifyDataSetChanged()
            when (item.itemId) {
                R.id.fragment_track -> {
                    binding.vPager.setCurrentItem(0, true)
                    true
                }

                R.id.fragment_stats -> {
                    binding.vPager.setCurrentItem(1, true)
                    true
                }

                R.id.fragment_history -> {
                    binding.vPager.setCurrentItem(2, true)
                    true
                }
                else -> true
            }
        }

        binding.nvNavigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.new_job_session -> {
                    showCreateDialog(viewModel)
                    true
                }

                R.id.view_job_sessions -> {
                    startActivity(Intent(this, JobSessionsActivity::class.java))
                    true
                }

                R.id.settings -> {
                    true
                }

                R.id.export -> {
                    startActivity(Intent(this, ExportActivity::class.java))
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

    inner class TrackingPagerAdapter(fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment = when (position) {
            Tab.TRACKING.ordinal -> TrackLoadFragment()
            Tab.STATS.ordinal -> StatisticsFragment()
            Tab.HISTORY.ordinal -> TrackingHistoryFragment()
            else -> throw IndexOutOfBoundsException("Fragment not found")
        }

        override fun getCount(): Int = Tab.values().size

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }
    }
}