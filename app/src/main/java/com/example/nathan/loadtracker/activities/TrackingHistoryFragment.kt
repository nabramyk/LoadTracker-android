package com.example.nathan.loadtracker.activities

import android.content.Intent
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_tracking_history.*

import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.arrayadapters.TrackingHistoryAdapter
import com.example.nathan.loadtracker.database
import com.example.nathan.loadtracker.models.Load

class TrackingHistoryActivity : AppCompatActivity() {

    private var sessionTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking_history)

        sessionTitle = intent.extras!!.getString("session_title_index")

        title = "Load History: " + sessionTitle!!

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_arrow_back_black_36px))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val loads = database.getLoadsForSession(sessionTitle!!) as ArrayList<Load>
        val listAdapter = TrackingHistoryAdapter(this, loads)
        trackedLoadHistory.adapter = listAdapter
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.tracking_history_context_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val upIntent = Intent(this, TrackingActivity::class.java)
                upIntent.putExtra("session_title_index", sessionTitle!!.toString())
                upIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                NavUtils.navigateUpTo(this, upIntent)
            }
        }
        return true
    }

    
}
