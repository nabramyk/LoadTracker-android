package com.example.nathan.loadtracker.activities

import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter

import com.example.nathan.loadtracker.DatabaseOpenHelper
import com.example.nathan.loadtracker.Item
import com.example.nathan.loadtracker.models.JobSession
import com.example.nathan.loadtracker.arrayadapters.JobSessionArrayAdapter
import com.example.nathan.loadtracker.jobsession.JobSessionItem
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.database
import kotlinx.android.synthetic.main.activity_job_sessions.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

class JobSessionsActivity : AppCompatActivity() {

    private var listAdapter: ArrayAdapter<Item>? = null
    lateinit var jobs: MutableList<JobSession>
    lateinit var jobsList: List<Item>

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
                listAdapter!!.remove(listAdapter!!.getItem(info.position))
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
        jobs = database.use {
            val rowParser = classParser<JobSession>()
            select(DatabaseOpenHelper.jobSessionsTable).parseList(rowParser).toMutableList()
        }

        jobsList = jobs.map {
            JobSessionItem(it)
        }

        listAdapter = JobSessionArrayAdapter(this, jobsList)
        jobSessionListView!!.isLongClickable = true
        registerForContextMenu(jobSessionListView)

        jobSessionListView!!.onItemClickListener = AdapterView.OnItemClickListener { _, view, position, _ ->
            val intent = Intent(view.context, TrackingActivity::class.java)
            intent.putExtra("session_title_index", jobs[position].jobTitle)
            startActivity(intent)
        }

        jobSessionListView!!.adapter = listAdapter
    }
}
