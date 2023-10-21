package com.example.nathan.loadtracker.ui.views

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.databinding.FragmentExportBinding
import com.example.nathan.loadtracker.ui.viewmodels.MainViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.number
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
                val adapter = SessionArrayAdapter(
                    requireContext(),
                    R.layout.list_item_job_session,
                    jobSessions
                )
                binding.sSession.adapter = adapter
            }
        }

        binding.sStartDate.setOnClickListener {
            DatePickerFragment(binding.sStartDate).show(parentFragmentManager, "export_start_date")
        }

        binding.sEndDate.setOnClickListener {
            DatePickerFragment(binding.sEndDate).show(parentFragmentManager, "export_end_date")
        }

        binding.sStartTime.setOnClickListener {
            TimePickerFragment(0, 0, binding.sStartTime).show(
                parentFragmentManager,
                "export_start_time"
            )
        }

        binding.sEndTime.setOnClickListener {
            TimePickerFragment(0, 0, binding.sEndTime).show(
                parentFragmentManager,
                "export_end_time"
            )
        }

        binding.cbCurrentDate.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val today = viewModel.now()
                binding.sStartDate.setText("${Month.values()[today.month.number-1]} ${today.dayOfMonth}, ${today.year}")
                binding.sEndDate.setText("${Month.values()[today.month.number-1]} ${today.dayOfMonth}, ${today.year}")
                binding.sStartTime.setText("00:00 A.M.")
                binding.sEndTime.setText("11:59 P.M.")
            } else {
                binding.sStartDate.setText("")
                binding.sEndDate.setText("")
                binding.sStartTime.setText("")
                binding.sEndTime.setText("")
            }
        }

        binding.bExport.setOnClickListener {
            var missingContent = false
            if (binding.sStartDate.text?.isEmpty() == true) {
                binding.sStartDate.error = ""
                missingContent = true
            }
            if (binding.sEndDate.text?.isEmpty() == true) {
                binding.sEndDate.error = ""
                missingContent = true
            }
            if (binding.sStartTime.text?.isEmpty() == true) {
                binding.sStartTime.error = ""
                missingContent = true
            }
            if (binding.sEndTime.text?.isEmpty() == true) {
                binding.sEndTime.error = ""
                missingContent = true
            }

            if (missingContent) {
                return@setOnClickListener
            }

            viewModel.jobSessionToExport.value?.let {
                val file = File(requireContext().cacheDir, "output.csv")
                FileWriter(file).apply {
                    // Converting the string to CharArrays is so far the only way I've figured out
                    // to get the entries to properly newline in the .csv
                    write("Id, Material, Driver, Title\n".toCharArray())

                    it.loads.forEach { load ->
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
        }
    }

    inner class SessionArrayAdapter(
        context: Context,
        layout: Int,
        private val jobSessions: List<JobSession>
    ) : ArrayAdapter<JobSession>(context, layout, jobSessions), OnItemSelectedListener {

        private var inflater: LayoutInflater = LayoutInflater.from(context)

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view: View? = convertView
            if (view == null) {
                view = inflater.inflate(R.layout.list_item_job_session, parent, false)
            }
            (view?.findViewById(R.id.tvJobSession) as TextView).text = getItem(position)!!.jobTitle
            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view: View? = convertView
            if (view == null) {
                view = inflater.inflate(R.layout.list_item_job_session, parent, false)
            }
            (view?.findViewById(R.id.tvJobSession) as TextView).text = getItem(position)!!.jobTitle
            return view
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            viewModel.selectJobSessionToExport(jobSessions[position].id)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
}

class TimePickerFragment(
    private val hour: Int,
    private val minute: Int,
    val context: TextInputEditText
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
        val time = LocalDateTime(
            year = 0,
            month = Month(1),
            dayOfMonth = 1,
            hour = hourOfDay,
            minute = minute,
            second = 0,
            nanosecond = 0,
        )

        context.setText("${time.hour}:${time.minute}")
    }
}

class DatePickerFragment(
    val context: TextInputEditText
) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val currentTime = viewModel.now()
        return DatePickerDialog(
            requireContext(),
            this,
            currentTime.year,
            currentTime.monthNumber - 1,
            currentTime.dayOfMonth
        )
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = LocalDateTime(
            year = year,
            month = Month(month + 1), //The DatePicker numbers months from 1-12
            dayOfMonth = dayOfMonth,
            hour = 0,
            minute = 0,
            second = 0,
            nanosecond = 0
        )

        context.setText(
            "${
                Month.values()[date.month.number]
            } ${date.dayOfMonth}, ${date.year} "
        )
    }
}
