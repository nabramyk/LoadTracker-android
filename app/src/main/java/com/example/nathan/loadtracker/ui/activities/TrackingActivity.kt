package com.example.nathan.loadtracker.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.databinding.ActivityTrackingBinding
import com.example.nathan.loadtracker.ui.viewmodels.TrackingViewModel

class TrackingActivity : AppCompatActivity() {

    private lateinit var vAdapter : TrackingPagerAdapter

    private lateinit var binding: ActivityTrackingBinding
    private val viewModel: TrackingViewModel by lazy {
        ViewModelProvider(this, TrackingViewModel.Factory(application))[TrackingViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_arrow_back_black_36px))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val jobSessionId = intent.getLongExtra("job_session_id", 0)
        viewModel.selectJobSession(jobSessionId)

        vAdapter = TrackingPagerAdapter(supportFragmentManager)
        binding.vPager.adapter = vAdapter
        binding.vPager.currentItem = Tab.TRACKING.ordinal

        binding.vPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                binding.bottomNavigation.selectedItemId = when (position) {
                    0 -> R.id.fragment_track
                    1 -> R.id.fragment_stats
                    else -> R.id.fragment_history
                }
            }
        })

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            vAdapter.notifyDataSetChanged()
            when(it.itemId) {
                R.id.fragment_track -> {
                    binding.vPager.setCurrentItem(0, true)
                    true
                }
                R.id.fragment_stats -> {
                    binding.vPager.setCurrentItem(1, true)
                    true
                }
                R.id.fragment_history -> {
                    binding.vPager.setCurrentItem(2, true)
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
