package com.example.nathan.loadtracker.ui.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.nathan.loadtracker.core.database.entities.Load
import com.example.nathan.loadtracker.databinding.FragmentStatisticsBinding
import com.example.nathan.loadtracker.ui.utils.DataFormatter
import com.example.nathan.loadtracker.ui.viewmodels.TrackingSessionViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.time.Duration

class StatisticsFragment : Fragment() {

    private lateinit var sessionTitle: String
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TrackingSessionViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionTitle = activity?.title?.toString()!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.loadsForActiveJobSession.collect { loads ->
                    updateAverageRunTime(loads)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.getLoadsAmalgamation.collect {
                    binding.tvTotalLoads.text = it.joinToString(separator = "\n") { it.material + it.count }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * TODO
     *  Handle the situation when loads span multiple days, i.e. do not calculate the
     *  difference from the last of one day and the first load of the next day
     */
    private fun updateAverageRunTime(loads: List<Load>) {
        binding.tvAverageRunTime.text = "${DataFormatter.calculateAverageRunTime(loads)}"
    }
}