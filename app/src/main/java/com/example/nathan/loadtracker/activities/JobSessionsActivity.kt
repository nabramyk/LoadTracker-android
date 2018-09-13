package com.example.nathan.loadtracker.activities

import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter

import com.example.nathan.loadtracker.DatabaseOpenHelper
import com.example.nathan.loadtracker.Item
import com.example.nathan.loadtracker.jobsession.JobSession
import com.example.nathan.loadtracker.arrayadapters.JobSessionArrayAdapter
import com.example.nathan.loadtracker.jobsession.JobSessionItem
import com.example.nathan.loadtracker.R
import kotlinx.android.synthetic.main.activity_job_sessions.*
import org.jetbrains.anko.db.RowParser
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

import java.util.ArrayList

class JobSessionsActivity : AppCompatActivity() {

    private var listAdapter: ArrayAdapter<Item>? = null
    lateinit var jobs: MutableList<JobSession>
    lateinit var jobsList: ArrayList<Item>

    private val db = DatabaseOpenHelper.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_sessions)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Open Job Sessions"

        //val db = DatabaseHandler(this)
        jobs = db.use {
            val rowParser = classParser<JobSession>()
            select(DatabaseOpenHelper.jobSessionsTable).parseList(rowParser).toMutableList()
        }
        jobsList = ArrayList()

        for (j in jobs) {
            jobsList.add(JobSessionItem(j))
            Log.d("JobSession: ", j.toString())
        }

        listAdapter = JobSessionArrayAdapter(this, jobsList)
        jobSessionListView!!.isLongClickable = true
        registerForContextMenu(jobSessionListView)

        jobSessionListView!!.onItemClickListener = AdapterView.OnItemClickListener { _, view, position, id ->
            val intent = Intent(view.context, TrackingActivity::class.java)
            intent.putExtra("session_title_index", jobs[position].jobTitle)
            startActivity(intent)
        }

        jobSessionListView!!.adapter = listAdapter
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId) {
            R.id.edit_session_entry,

            R.id.close_session_entry -> {
                //val handler = DatabaseHandler(baseContext)
                listAdapter!!.remove(listAdapter!!.getItem(info.position))
                //handler.deleteJobSession(jobs[info.position])
                jobs.removeAt(info.position)
                db.use {
                    delete(DatabaseOpenHelper.jobSessionsTable, DatabaseOpenHelper.columnId + " = " + jobs[info.position].id, null)
                }
                super.onContextItemSelected(item)
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }
}
