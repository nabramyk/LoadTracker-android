package com.example.nathan.loadtracker.ui.views

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.core.content.FileProvider
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.databinding.FragmentExportBinding
import com.example.nathan.loadtracker.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.toLocalDateTime
import java.io.File
import java.io.FileWriter

class ExportFragment : Fragment() {

    private var _binding: FragmentExportBinding? = null
    private val viewModel: MainViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExportBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.allJobSessions.collect { jobSessions ->
                val adapter = ArrayAdapter<Pair<String, Long>>(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item
                )
                for (js in jobSessions) {
                    adapter.add(Pair(js.jobTitle.toString(), js.id))
                }
                binding.sSession.adapter = adapter

                binding.sSession.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View,
                            i: Int,
                            l: Long
                        ) {
                            viewModel.selectJobSessionToExport(jobSessions[i].id)
                        }

                        override fun onNothingSelected(adapterView: AdapterView<*>) {}
                    }
            }
        }

        viewModel.jobSessionToExport.observe(viewLifecycleOwner) { jobSessionWithLoads ->
            Log.d("exportable job session", jobSessionWithLoads.toString())
            val dateAdapter =
                ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item
                )
            jobSessionWithLoads.loads.asSequence().map { it.timeLoaded.toString() }.distinct()
                .toList()
                .forEach { dateAdapter.add(it) }

            binding.sStartDate.adapter = dateAdapter
            binding.sEndDate.adapter = dateAdapter

            val timeAdapter =
                ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item
                )
            jobSessionWithLoads.loads.asSequence().map { it.timeLoaded.toString() }.distinct()
                .toList()
                .forEach { timeAdapter.add(it) }

            binding.sStartTime.adapter = timeAdapter
            binding.sEndTime.adapter = timeAdapter
        }

        binding.bExport.setOnClickListener {
            val file = File(requireContext().cacheDir, "output.csv")
            FileWriter(file).apply {
                // Converting the string to CharArrays is so far the only way I've figured out
                // to get the entries to properly newline in the .csv
                write("Id, Material, Driver, Title\n".toCharArray())

                viewModel.jobSessionToExport.value!!.loads.forEach { load ->
                    write("${load.id}, ${load.material}, ${load.driver}, ${viewModel.jobSessionToExport.value!!.jobSession.jobTitle}\n".toCharArray())
                }

                close()

                // I originally was using `createChooser` but it was throwing some error messages
                // about file permissions. This could be cleaner but it works for the time being.
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    putExtra(
                        Intent.EXTRA_STREAM,
                        FileProvider.getUriForFile(
                            requireContext(),
                            getString(R.string.file_provider_authority),
                            file
                        )
                    )
                }
                startActivity(intent)
            }
        }

        binding.cbCurrentDate.setOnClickListener {
            binding.sStartDate.isEnabled = !binding.cbCurrentDate.isChecked
            binding.sEndDate.isEnabled = !binding.cbCurrentDate.isChecked
            binding.sStartTime.isEnabled = !binding.cbCurrentDate.isChecked
            binding.sEndTime.isEnabled = !binding.cbCurrentDate.isChecked
        }
    }

    inner class TimePickerFragment(
        private val hour: Int,
        private val minute: Int,
        val context: Int
    ) : DialogFragment(),
        TimePickerDialog.OnTimeSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return TimePickerDialog(
                requireContext(),
                this,
                hour,
                minute,
                DateFormat.is24HourFormat(requireActivity())
            )
        }

        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        }
    }
}
