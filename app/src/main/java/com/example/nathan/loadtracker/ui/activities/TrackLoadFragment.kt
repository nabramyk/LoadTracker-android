package com.example.nathan.loadtracker.ui.activities

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.nathan.loadtracker.databinding.FragmentLoadTrackingBinding
import com.example.nathan.loadtracker.ui.viewmodels.TrackingViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar

class TrackLoadFragment : Fragment() {

    private lateinit var sessionTitle: String

    private var _binding: FragmentLoadTrackingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TrackingViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionTitle = activity?.title?.toString()!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadTrackingBinding.inflate(inflater, container, false)
        return binding.root
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

            val c = Calendar.getInstance()

            binding.apply {
                viewModel.addLoad(
                    driver = driverNameInput.text.toString(),
                    unitId = unitIDInput.text.toString(),
                    material = materialInput.text.toString(),
                    timestamp = c.time,
                    companyName = companyNameInput.text.toString(),
                )
            }

            Snackbar.make(view, "Tracked!", Snackbar.LENGTH_LONG).show()
        }

        viewModel.selectedJobSession.observe(viewLifecycleOwner) { js ->
            if (js.loads.isNotEmpty()) {
                js.loads.let {
                    binding.materialInput.setText(it[it.size - 1].material)
                    binding.unitIDInput.setText(it[it.size - 1].unitId)
                    binding.driverNameInput.setText(it[it.size - 1].driver)
                    binding.companyNameInput.setText(it[it.size - 1].companyName)
                }
            } else {
                val sharedPrefs = activity?.getSharedPreferences(
                    "com.example.nathan.loadtracker",
                    Context.MODE_PRIVATE
                )
                binding.driverNameInput.setText(sharedPrefs?.getString("name", ""))
                binding.companyNameInput.setText(sharedPrefs?.getString("company", ""))
            }
        }
    }
}