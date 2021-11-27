package com.example.nathan.loadtracker.ui.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.*

import com.example.nathan.loadtracker.core.database.LoadTrackerDatabase
import com.example.nathan.loadtracker.ui.arrayadapters.TrackingHistoryAdapter
import com.example.nathan.loadtracker.core.database.entities.Load
import com.example.nathan.loadtracker.databinding.FragmentTrackingHistoryBinding

class TrackingHistoryFragment : Fragment() {

    private lateinit var sessionTitle: String
    private lateinit var loads: List<Load>
    private var _binding: FragmentTrackingHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionTitle = activity?.title?.toString()!!
        loads = LoadTrackerDatabase.getLoadsForSession(sessionTitle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTrackingHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listAdapter = TrackingHistoryAdapter(loads)
        binding.trackedLoadHistory.layoutManager = LinearLayoutManager(context)
        binding.trackedLoadHistory.adapter = listAdapter
    }
}
