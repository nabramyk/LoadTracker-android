package com.example.nathan.loadtracker.activities

import android.content.Intent
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_tracking_history.*

import com.example.nathan.loadtracker.Item
import com.example.nathan.loadtracker.load.LoadListItem
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.arrayadapters.TrackingHistoryArrayAdapter
import com.example.nathan.loadtracker.database

class TrackingHistoryActivity : AppCompatActivity() {

    private var listAdapter: ArrayAdapter<Item>? = null
    private var sessionTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking_history)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        sessionTitle = intent.extras!!.getString("session_title_index")

        title = "Load History: " + sessionTitle!!

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_arrow_back_black_36px))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        registerForContextMenu(trackedLoadHistory)
        trackedLoadHistory!!.isLongClickable = true

        val loads = database.getLoadsForSession(sessionTitle!!)

        val loadsList = loads.map { LoadListItem(it) }

        listAdapter = TrackingHistoryArrayAdapter(this, loadsList)
        trackedLoadHistory!!.adapter = listAdapter
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.tracking_history_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
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
                upIntent.putExtra("session_title_index", sessionTitle!!.toString())
                upIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                NavUtils.navigateUpTo(this, upIntent)
            }
        }
        return true
    }
}
