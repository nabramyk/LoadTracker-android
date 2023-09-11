package com.example.nathan.loadtracker.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.MenuItem
import android.widget.AdapterView
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.ui.arrayadapters.JobSessionAdapter
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import com.example.nathan.loadtracker.databinding.ActivityJobSessionsBinding
import com.example.nathan.loadtracker.ui.viewmodels.TrackingViewModel

class JobSessionsActivity : AppCompatActivity() {

    private lateinit var jobs: ArrayList<JobSession>
    private lateinit var binding: ActivityJobSessionsBinding

    private val viewModel: TrackingViewModel by lazy {
        ViewModelProvider(
            this,
            TrackingViewModel.Factory(application)
        )[TrackingViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobSessionsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Open Job Sessions"

        viewModel.allJobSessionsWithLoads.observe(this) { jobSessionsWithLoads ->
            populateJobSessionsList(jobSessionsWithLoads)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId) {
            R.id.edit_session_entry,

            R.id.close_session_entry -> {
                jobs.removeAt(info.position)
                super.onContextItemSelected(item)
            }

            else -> super.onContextItemSelected(item)
        }
    }

    private fun populateJobSessionsList(jobs: List<JobSessionWithLoads>) {
        val listAdapter = JobSessionAdapter(this, jobs as ArrayList<JobSessionWithLoads>) { selectedJobSession ->
            viewModel.selectJobSession(selectedJobSession)
            startActivity(
                Intent(this, TrackingActivity::class.java)
                    .putExtra("session_title_index", selectedJobSession.jobSession.jobTitle),
                ActivityOptionsCompat.makeBasic().toBundle()
            )
            Log.d("CLicked", "clicked")
        }

        binding.rvJobSessions.layoutManager = LinearLayoutManager(this)
        registerForContextMenu(binding.rvJobSessions)

        binding.rvJobSessions.adapter = listAdapter
    }
}
