package com.example.nathan.loadtracker.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nathan.loadtracker.databinding.FragmentTrackingHistoryBinding
import com.example.nathan.loadtracker.ui.arrayadapters.TrackingHistoryAdapter
import com.example.nathan.loadtracker.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch

class TrackingHistoryFragment : Fragment() {

    private lateinit var sessionTitle: String
    private var _binding: FragmentTrackingHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionTitle = activity?.title?.toString()!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackingHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listAdapter = TrackingHistoryAdapter(emptyList())
        binding.trackedLoadHistory.layoutManager = LinearLayoutManager(context)
        binding.trackedLoadHistory.adapter = listAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.getLoadsForActiveJobSession().collect { loads ->
                    listAdapter.loads = loads
                    listAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}
