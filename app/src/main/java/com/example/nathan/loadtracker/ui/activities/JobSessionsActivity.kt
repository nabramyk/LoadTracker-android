package com.example.nathan.loadtracker.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.MenuItem
import android.widget.AdapterView
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.ui.arrayadapters.JobSessionAdapter
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.core.database.LoadTrackerDatabase
import com.example.nathan.loadtracker.databinding.ActivityJobSessionsBinding

class JobSessionsActivity : AppCompatActivity() {

    private lateinit var jobs: ArrayList<JobSession>
    private lateinit var binding: ActivityJobSessionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobSessionsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Open Job Sessions"

        populateJobSessionsList()
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

    private fun populateJobSessionsList() {
        jobs = LoadTrackerDatabase.getJobSessions() as ArrayList<JobSession>

        val listAdapter = JobSessionAdapter(this, jobs)
        binding.rvJobSessions.layoutManager = LinearLayoutManager(this)
        registerForContextMenu(binding.rvJobSessions)

        binding.rvJobSessions.adapter = listAdapter
    }
}
