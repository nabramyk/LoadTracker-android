package com.example.nathan.loadtracker.activities

import android.content.Intent
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ListView

import com.example.nathan.loadtracker.Item
import com.example.nathan.loadtracker.load.Load
import com.example.nathan.loadtracker.load.LoadListItem
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.arrayadapters.TrackingHistoryArrayAdapter

import java.util.ArrayList

class TrackingHistoryActivity : AppCompatActivity() {

    private var trackedLoadsHistory: ListView? = null
    private var loads: List<Load>? = null
    private var listAdapter: TrackingHistoryArrayAdapter? = null
    private var session_title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tracking_history_activity)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        session_title = intent.extras!!.getString("session_title_index")

        title = "Load History: " + session_title!!

        val myToolbar = findViewById(R.id.history_activity_toolbar) as Toolbar
        setSupportActionBar(myToolbar)

        val ab = supportActionBar
        ab!!.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_arrow_back_black_36px))
        ab.setDisplayHomeAsUpEnabled(true)

        trackedLoadsHistory = findViewById(R.id.trackedLoadHistory) as ListView
        registerForContextMenu(trackedLoadsHistory)
        trackedLoadsHistory!!.isLongClickable = true
        //val db = DatabaseHandler(this)
        //loads = db.getAllLoadsForSession(intent.extras!!.getString("session_title_index"))

        val loadsList = ArrayList<Item>()

        for (l in loads!!) {
            if (loads!!.indexOf(l) <= 0) {
                loadsList.add(LoadListItem(l))
            } else if (l.dateLoaded != loads!![loads!!.indexOf(l) - 1].dateLoaded) {
                loadsList.add(LoadListItem(l))
            } else
                loadsList.add(LoadListItem(l))

        }

        listAdapter = TrackingHistoryArrayAdapter(this, loadsList)
        trackedLoadsHistory!!.adapter = listAdapter
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.tracking_history_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        //val db = DatabaseHandler(this)
        val temp = listAdapter!!.getItem(info.position) as LoadListItem
        if (listAdapter!!.getItemViewType(info.position) == 1) {
            return super.onContextItemSelected(item)
        }
        when (item.itemId) {
            R.id.edit_session_entry,

            R.id.remove_session_entry -> {
                //Log.d("Load:", String.valueOf(info.id));
                //db.deleteLoadFromSession(temp.id)
                listAdapter!!.remove(listAdapter!!.getItem(info.position))
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val upIntent = Intent(this, TrackingActivity::class.java)
                upIntent.putExtra("session_title_index", session_title!!.toString())
                upIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                NavUtils.navigateUpTo(this, upIntent)
            }
        }
        return true
    }
}
