package com.example.nathan.loadtracker.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        lifecycleScope.launch {
            viewModel.allJobSessions.collect { jobs ->
                val listAdapter =
                    JobSessionAdapter(
                        requireContext(),
                        jobs as ArrayList<JobSession>
                    ) { selectedJobSession ->
                        viewModel.selectJobSession(selectedJobSession.id)
                        findNavController().popBackStack()
                    }

                binding.rvJobSessions.layoutManager = LinearLayoutManager(requireContext())
                registerForContextMenu(binding.rvJobSessions)

                binding.rvJobSessions.adapter = listAdapter
            }
        }
    }
}
