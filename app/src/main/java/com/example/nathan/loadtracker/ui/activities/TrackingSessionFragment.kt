package com.example.nathan.loadtracker.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.databinding.FragmentTrackingSessionBinding

class TrackingSessionFragment : Fragment() {

    private var _binding: FragmentTrackingSessionBinding? = null
    private val binding get() = _binding!!

    private lateinit var vAdapter: TrackingPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackingSessionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vAdapter = TrackingPagerAdapter(childFragmentManager)
        binding.vPager.adapter = vAdapter
        binding.vPager.currentItem = MainActivity.Tab.TRACKING.ordinal

        binding.vPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                binding.bottomNavigation.selectedItemId = when (position) {
                    0 -> R.id.fragment_track
                    1 -> R.id.fragment_stats
                    else -> R.id.fragment_history
                }
            }
        })

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            vAdapter.notifyDataSetChanged()
            when (item.itemId) {
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

    inner class TrackingPagerAdapter(fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment = when (position) {
            MainActivity.Tab.TRACKING.ordinal -> TrackLoadFragment()
            MainActivity.Tab.STATS.ordinal -> StatisticsFragment()
            MainActivity.Tab.HISTORY.ordinal -> TrackingHistoryFragment()
            else -> throw IndexOutOfBoundsException("Fragment not found")
        }

        override fun getCount(): Int = MainActivity.Tab.values().size

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }
    }
}