package com.example.nathan.loadtracker.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nathan.loadtracker.LoadTrackerApplication.Companion.dataStore
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.databinding.FragmentExportBinding
import com.example.nathan.loadtracker.ui.arrayadapters.SessionArrayAdapter
import com.example.nathan.loadtracker.ui.utils.LoadTrackerCSVExporter
import com.example.nathan.loadtracker.ui.utils.DatePickerFragment
import com.example.nathan.loadtracker.ui.utils.TimePickerFragment
import com.example.nathan.loadtracker.ui.viewmodels.ExportViewModel

class ExportFragment : Fragment() {

    private var _binding: FragmentExportBinding? = null
    private val viewModel: ExportViewModel by viewModels {
        ExportViewModel.Factory(
            context = requireActivity().application,
            dataStore = requireActivity().applicationContext.dataStore
        )
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.allJobSessions.observe(viewLifecycleOwner) { jobSessions ->
            // I'd rather declare this once outside of the observer, but I think the array adapter
            // doesn't have the proper notification and update functionality so this should change
            val adapter = SessionArrayAdapter(
                requireContext(),
                R.layout.list_item_job_session,
                jobSessions,
                viewModel::selectJobSessionToExport
            )
            binding.sSession.adapter = adapter
        }

        viewModel.startDate.observe(viewLifecycleOwner) {
            binding.sStartDate.setText(it)
        }

        viewModel.endDate.observe(viewLifecycleOwner) {
            binding.sEndDate.setText(it)
        }

        viewModel.startTime.observe(viewLifecycleOwner) {
            binding.sStartTime.setText(it)
        }

        viewModel.endTime.observe(viewLifecycleOwner) {
            binding.sEndTime.setText(it)
        }

        binding.sStartDate.setOnClickListener {
            DatePickerFragment(viewModel::updateStartDate).show(parentFragmentManager, "export_start_date")
        }

        binding.sEndDate.setOnClickListener {
            DatePickerFragment(viewModel::updateEndDate).show(parentFragmentManager, "export_end_date")
        }

        binding.sStartTime.setOnClickListener {
            TimePickerFragment(0, 0, viewModel::updateStartTime).show(
                parentFragmentManager,
                "export_start_time"
            )
        }

        binding.sEndTime.setOnClickListener {
            TimePickerFragment(0, 0, viewModel::updateEndTime).show(
                parentFragmentManager,
                "export_end_time"
            )
        }

        binding.cbCurrentDate.setOnCheckedChangeListener { buttonView, isChecked ->

        }

        binding.bExport.setOnClickListener {
            LoadTrackerCSVExporter.export(requireContext(), viewModel.jobSessionToExport.value!!)
        }
    }
}
