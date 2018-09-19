package com.example.nathan.loadtracker.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.database
import kotlinx.android.synthetic.main.activity_tracking.*

import java.util.HashMap

class TrackingActivity : AppCompatActivity() {

    private lateinit var sessionTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        setSupportActionBar(toolbar)

        supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_arrow_back_black_36px))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sessionTitle = intent.getStringExtra("session_title_index")
        title = sessionTitle

        vPager.adapter = TrackingPagerAdapter(supportFragmentManager)
        vPager.currentItem = Tab.TRACKING.ordinal

//        bottom_navigation.setOnNavigationItemSelectedListener {
//            when(it.itemId) {
//                R.id.fragment_track -> { true }
//                R.id.fragment_stats -> { true }
//                R.id.fragment_history -> { true }
//                else -> true
//            }
//        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.intent = intent
    }

    private fun updateTotalLoadsTracked() {
        val loads = database.getLoadsForSession(sessionTitle)
        val materials = HashMap<String, Int>()

        for (l in loads) {
            if (!materials.containsKey(l.material)) materials[l.material!!] = 1
            else if (materials.containsKey(l.material)) materials[l.material!!] = materials[l.material!!]!! + 1
        }

        var formattedOutput = materials.toString()
        formattedOutput = formattedOutput.replace("=", ": ")
        formattedOutput = formattedOutput.replace(",", "\n")
        formattedOutput = formattedOutput.replace("{", "")
        formattedOutput = formattedOutput.replace("}", "")

       // nav_view.menu.add(formattedOutput)
    }

    private fun updateAverageRunTime() {
        val loads = database.getLoadsForSession(title.toString())
        if (loads.isEmpty()) {
        //    nav_view.menu.add("00:00:00.000")
            return
        }
        var hours = 0
        var minutes = 0
        var seconds = 0
        var milliseconds = 0
        for (l in loads) {
            var timeLoaded = l.timeLoaded
            timeLoaded = timeLoaded!!.replace(":", " ")
            timeLoaded = timeLoaded.replace(".", " ")
            val components = timeLoaded.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            hours += Integer.parseInt(components[0])
            minutes += Integer.parseInt(components[1])
            seconds += Integer.parseInt(components[2])
            milliseconds += Integer.parseInt(components[3])
        }
        hours /= loads.size
        minutes /= loads.size
        seconds /= loads.size
        milliseconds /= loads.size

       // nav_view.menu.add(hours.toString() + ":" + minutes + ":" + seconds + "." + milliseconds)
    }

    enum class Tab {
        TRACKING,
        STATS,
        HISTORY
    }

    inner class TrackingPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment = when (position) {
            Tab.TRACKING.ordinal -> TrackLoadFragment()
            Tab.STATS.ordinal -> StatisticsFragment()
            Tab.HISTORY.ordinal -> TrackingHistoryFragment()
            else -> throw IndexOutOfBoundsException("Fragment not found")
        }

        override fun getCount(): Int = Tab.values().size
    }
}
