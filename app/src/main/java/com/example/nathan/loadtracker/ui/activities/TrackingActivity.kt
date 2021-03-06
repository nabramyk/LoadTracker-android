package com.example.nathan.loadtracker.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

import com.example.nathan.loadtracker.R
import kotlinx.android.synthetic.main.activity_tracking.*

class TrackingActivity : AppCompatActivity() {

    private lateinit var sessionTitle: String
    lateinit var vAdapter : TrackingPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        setSupportActionBar(toolbar)

        supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_arrow_back_black_36px))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sessionTitle = intent.getStringExtra("session_title_index")
        title = sessionTitle

        vAdapter = TrackingPagerAdapter(supportFragmentManager)
        vPager.adapter = vAdapter
        vPager.currentItem = Tab.TRACKING.ordinal

        vPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                bottom_navigation.selectedItemId = when (position) {
                    0 -> R.id.fragment_track
                    1 -> R.id.fragment_stats
                    else -> R.id.fragment_history
                }
            }
        })

        bottom_navigation.setOnNavigationItemSelectedListener {
            vAdapter.notifyDataSetChanged()
            when(it.itemId) {
                R.id.fragment_track -> {
                    vPager.setCurrentItem(0, true)
                    true
                }
                R.id.fragment_stats -> {
                    vPager.setCurrentItem(1, true)
                    true
                }
                R.id.fragment_history -> {
                    vPager.setCurrentItem(2, true)
                    true
                }
                else -> true
            }
        }
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

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }
    }
}
