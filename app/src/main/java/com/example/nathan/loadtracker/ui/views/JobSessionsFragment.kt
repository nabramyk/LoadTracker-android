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
import com.example.nathan.loadtracker.R
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
                if (!findNavController().popBackStack()) {
                    findNavController().navigate(R.id.action_global_trackingSessionFragment)
                }
            },
            { jobSession, isActiveSession ->
                AlertDialog
                    .Builder(requireContext())
                    .setMessage("Really?")
                    .setPositiveButton("Yep!") { dialog, _ ->
                        if (isActiveSession) {
                            dialog.dismiss()

                            AlertDialog
                                .Builder(requireContext())
                                .setMessage(
                                    "This session is currently active! If you close this now," +
                                            " you will have to pick another session or create a new one before proceeding. " +
                                            "Are you sure?"
                                )
                                .setPositiveButton("Yep!") { dialog, _ ->
                                    viewModel.deleteJobSession(jobSession)
                                    findNavController().clearBackStack(R.id.trackingSessionFragment)
                                    dialog.dismiss()
                                }
                                .setNegativeButton("Nah") { dialog, _ -> dialog.dismiss() }
                                .show()
                        } else {
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
            viewModel.jobSessionModelFlow.collect { model ->
                listAdapter.jobSessions =
                    model.allJobSessions.map {
                        Pair(it, it.id == model.activeJobSessionId)
                    } as ArrayList<Pair<JobSession, Boolean>>
                listAdapter.notifyDataSetChanged()
            }
        }
    }
}
