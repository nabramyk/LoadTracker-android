package com.example.nathan.loadtracker.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.widget.AdapterView

import com.example.nathan.loadtracker.DatabaseOpenHelper
import com.example.nathan.loadtracker.models.JobSession
import com.example.nathan.loadtracker.arrayadapters.JobSessionAdapter
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.database
import kotlinx.android.synthetic.main.activity_job_sessions.*

class JobSessionsActivity : AppCompatActivity() {

    private lateinit var jobs: ArrayList<JobSession>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_sessions)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Open Job Sessions"

        populateJobSessionsList()
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId) {
            R.id.edit_session_entry,

            R.id.close_session_entry -> {
                //listAdapter.remove(listAdapter.getItem(info.position))
                jobs.removeAt(info.position)
                database.use {
                    delete(DatabaseOpenHelper.jobSessionsTable, null, null)
                }
                super.onContextItemSelected(item)
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun populateJobSessionsList() {
        jobs = database.getJobSessions() as ArrayList<JobSession>

        val listAdapter = JobSessionAdapter(this, jobs)
        rvJobSessions.layoutManager = LinearLayoutManager(this)
        registerForContextMenu(rvJobSessions)

        rvJobSessions.adapter = listAdapter
    }
}
