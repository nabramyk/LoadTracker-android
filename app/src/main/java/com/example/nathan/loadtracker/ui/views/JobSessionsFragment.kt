package com.example.nathan.loadtracker.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.databinding.FragmentJobSessionsBinding
import com.example.nathan.loadtracker.ui.arrayadapters.JobSessionAdapter
import com.example.nathan.loadtracker.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch

class JobSessionsFragment : Fragment() {

    private var _binding: FragmentJobSessionsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobSessionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listAdapter = JobSessionAdapter(
            arrayListOf(),
            { selectedJobSession ->
                viewModel.selectJobSession(selectedJobSession.id)
                findNavController().popBackStack()
            },
            { jobSession ->
                viewModel.deleteJobSession(jobSession)
                            AlertDialog
                                .Builder(requireContext())
                                .setMessage("Really?")
                                .setPositiveButton("Yep!") { dialog, _ ->
                                    viewLifecycleOwner.lifecycleScope.launch {
                                        viewModel.deleteJobSession(jobSession)
                                        dialog.dismiss()
                                    }
                                }
                                .setNegativeButton("Nah") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
            }
        )
        binding.rvJobSessions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvJobSessions.adapter = listAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allJobSessions.collect { jobs ->
                listAdapter.jobSessions = jobs as ArrayList<JobSession>
                listAdapter.notifyDataSetChanged()
            }
        }
    }
}
