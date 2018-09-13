package com.example.nathan.loadtracker.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.nathan.loadtracker.DatabaseOpenHelper

import com.example.nathan.loadtracker.models.Load
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.database
import kotlinx.android.synthetic.main.tracking_activity.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.HashMap

class TrackingActivity : AppCompatActivity() {

    private lateinit var sessionTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tracking_activity)

        setSupportActionBar(toolbar)
        toolbar.overflowIcon = resources.getDrawable(R.drawable.ic_more_vert_black_36px)

        supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_arrow_back_black_36px))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sessionTitle = intent.getStringExtra("session_title_index")

        val js = database.getLoadsForSession(sessionTitle).toMutableList()

        if (js.isNotEmpty()) {
            materialInput.setText(js[js.size - 1].material)
            unitIDInput.setText(js[js.size - 1].unitId)
            driverNameInput.setText(js[js.size - 1].driver)
            companyNameInput.setText(js[js.size - 1].companyName)
        } else {
            val sharedPrefs = getSharedPreferences("com.example.nathan.loadtracker", Context.MODE_PRIVATE)
            driverNameInput.setText(sharedPrefs.getString("name", ""))
            companyNameInput.setText(sharedPrefs.getString("company", ""))
        }

        history_breakdown.text = updateTotalLoadsTracked()
        average_load_time.text = updateAverageRunTime()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.intent = intent
    }

    fun trackData(view: View) {

        if (materialInput.text.toString().trim { it <= ' ' }.isEmpty()) {
            Snackbar.make(view, "Missing material", Snackbar.LENGTH_LONG).show()
            return
        }

        if (unitIDInput.text.toString().trim { it <= ' ' }.isEmpty()) {
            Snackbar.make(view, "Missing unit ID", Snackbar.LENGTH_LONG).show()
            return
        }

        if (driverNameInput.text.toString().trim { it <= ' ' }.isEmpty()) {
            Snackbar.make(view, "Missing driver name", Snackbar.LENGTH_LONG).show()
            return
        }

        if (companyNameInput.text.toString().trim { it <= ' ' }.isEmpty()) {
            Snackbar.make(view, "Missing company name", Snackbar.LENGTH_LONG).show()
            return
        }

        val c = Calendar.getInstance()

        database.addLoad(
                Load(
                        id = null,
                        title = sessionTitle,
                        driver = driverNameInput.text.toString(),
                        unitId = unitIDInput.text.toString(),
                        material = materialInput.text.toString(),
                        companyName = companyNameInput.text.toString(),
                        timeLoaded = SimpleDateFormat("HH:mm:ss.SSS").format(c.time),
                        dateLoaded = SimpleDateFormat("yyyy/MM/dd").format(c.time)
                )
        )

        history_breakdown.text = updateTotalLoadsTracked()
        average_load_time.text = updateAverageRunTime()

    }

    private fun updateTotalLoadsTracked(): String {
        //val loads = db.getAllLoadsForSession(title.toString())
        val materials = HashMap<String, Int>()

//        for (l in loads) {
//            if (!materials.containsKey(l.material))
//                materials[l.material] = 1
//            else if (materials.containsKey(l.material))
//                materials[l.material] = materials[l.material]!! + 1
//        }

        var formattedOutput = materials.toString()
        formattedOutput = formattedOutput.replace("=", ": ")
        formattedOutput = formattedOutput.replace(",", "\n")
        formattedOutput = formattedOutput.replace("{", "")
        formattedOutput = formattedOutput.replace("}", "")

        return formattedOutput
    }

    private fun updateAverageRunTime(): String {
        //val loads = db.getAllLoadsForSession(title.toString())
//        if (loads.size == 0) {
//            return "00:00:00.000"
//        }
        var hours = 0
        var minutes = 0
        var seconds = 0
        var milliseconds = 0
//        for (l in loads) {
//            var timeLoaded = l.timeLoaded
//            timeLoaded = timeLoaded.replace(":", " ")
//            timeLoaded = timeLoaded.replace(".", " ")
//            Log.d("Time Loaded: ", timeLoaded)
//            val components = timeLoaded.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//            hours += Integer.parseInt(components[0])
//            minutes += Integer.parseInt(components[1])
//            seconds += Integer.parseInt(components[2])
//            milliseconds += Integer.parseInt(components[3])
//        }
//        hours /= loads.size
//        minutes /= loads.size
//        seconds /= loads.size
//        milliseconds /= loads.size

        return hours.toString() + ":" + minutes + ":" + seconds + "." + milliseconds
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.tracking_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.view_load_history -> {
                viewLoadTrackingHistory()
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun viewLoadTrackingHistory() {
        val intent = Intent(this, TrackingHistoryActivity::class.java)
        intent.putExtra("session_title_index", title.toString())
        startActivity(intent)
    }
}
