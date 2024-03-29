package com.example.nathan.loadtracker.ui.views

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.nathan.loadtracker.LoadTrackerApplication.Companion.dataStore
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.databinding.FragmentNewJobSessionBinding
import com.example.nathan.loadtracker.ui.viewmodels.NewJobSessionViewModel
import com.example.nathan.loadtracker.ui.viewmodels.TrackingSessionViewModel

class NewJobSessionFragment : Fragment() {

    private var _binding: FragmentNewJobSessionBinding? = null

    private val binding get() = _binding!!
    private val viewModel: NewJobSessionViewModel by viewModels {
        NewJobSessionViewModel.Factory(
            context = requireActivity().application,
            dataStore = requireActivity().applicationContext.dataStore
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewJobSessionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sessionTitleEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 0) {
                    binding.sessionTitleEditText.error = "Session title must not be empty"
                }
            }
        })

        binding.bCreate.setOnClickListener {
            if (!TextUtils.isEmpty(binding.sessionTitleEditText.text)) {
                viewModel.addJobSession(
                    jobTitle = binding.sessionTitleEditText.text.toString()
                )
                view.findNavController().navigate(R.id.action_global_trackingSessionFragment)
            }
        }
    }
}