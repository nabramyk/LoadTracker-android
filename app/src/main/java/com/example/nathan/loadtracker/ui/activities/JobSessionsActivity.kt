package com.example.nathan.loadtracker.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.databinding.ActivityJobSessionsBinding
import com.example.nathan.loadtracker.ui.arrayadapters.JobSessionAdapter
import com.example.nathan.loadtracker.ui.viewmodels.JobSessionsViewModel

class JobSessionsActivity : AppCompatActivity() {

    private lateinit var jobs: ArrayList<JobSession>
    private lateinit var binding: ActivityJobSessionsBinding

    private val viewModel: JobSessionsViewModel by lazy {
        ViewModelProvider(this, JobSessionsViewModel.Factory(application))[JobSessionsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobSessionsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Open Job Sessions"

        viewModel.allJobSessions.observe(this) { jobSessions ->
            populateJobSessionsList(jobSessions)
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

    private fun populateJobSessionsList(jobs: List<JobSession>) {
        val listAdapter = JobSessionAdapter(this, jobs as ArrayList<JobSession>) { selectedJobSession ->
            startActivity(
                Intent(this, TrackingActivity::class.java)
                    .putExtra("job_session_id", selectedJobSession.id),
                ActivityOptionsCompat.makeBasic().toBundle()
            )
        }

        binding.rvJobSessions.layoutManager = LinearLayoutManager(this)
        registerForContextMenu(binding.rvJobSessions)

        binding.rvJobSessions.adapter = listAdapter
    }
}
