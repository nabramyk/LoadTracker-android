package com.example.nathan.loadtracker.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.nathan.loadtracker.databinding.FragmentLoadTrackingBinding
import com.example.nathan.loadtracker.ui.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class TrackLoadFragment : Fragment() {

    private var _binding: FragmentLoadTrackingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bTrack.setOnClickListener {
            if (binding.materialInput.text.toString().trim { it <= ' ' }.isEmpty()) {
                Snackbar.make(view, "Missing material", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (binding.unitIDInput.text.toString().trim { it <= ' ' }.isEmpty()) {
                Snackbar.make(view, "Missing unit ID", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (binding.driverNameInput.text.toString().trim { it <= ' ' }.isEmpty()) {
                Snackbar.make(view, "Missing driver name", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (binding.companyNameInput.text.toString().trim { it <= ' ' }.isEmpty()) {
                Snackbar.make(view, "Missing company name", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            binding.apply {
                viewModel.addLoad(
                    driver = driverNameInput.text.toString(),
                    unitId = unitIDInput.text.toString(),
                    material = materialInput.text.toString(),
                    companyName = companyNameInput.text.toString(),
                )
            }

            Snackbar.make(view, "Tracked!", Snackbar.LENGTH_LONG).show()
        }

        lifecycleScope.launch {
            viewModel.mainUiModel.collect { uiModel ->
                if (uiModel.activeJobSessionWithLoads?.loads?.isNotEmpty() == true) {
                    uiModel.activeJobSessionWithLoads.loads.let { loads ->
                        loads.let {
                            binding.materialInput.setText(it[it.size - 1].material)
                            binding.unitIDInput.setText(it[it.size - 1].unitId)
                            binding.driverNameInput.setText(it[it.size - 1].driver)
                            binding.companyNameInput.setText(it[it.size - 1].companyName)
                        }
                    }
                } else {
                    binding.driverNameInput.setText(uiModel.driverName)
                    binding.companyNameInput.setText(uiModel.companyName)
                }
            }
        }
    }
}
