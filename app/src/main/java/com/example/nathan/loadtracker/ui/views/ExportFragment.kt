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
import androidx.fragment.app.viewModels
import com.example.nathan.loadtracker.LoadTrackerApplication.Companion.dataStore
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.databinding.FragmentExportBinding
import com.example.nathan.loadtracker.ui.viewmodels.ExportViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.File
import java.io.FileWriter
import java.time.format.TextStyle
import java.util.Locale

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.allJobSessions.observe(viewLifecycleOwner) { jobSessions ->
            // I'd rather declare this once outside of the observer, but I think the array adapter
            // doesn't have the proper notification and update functionality so this should change
            val adapter = SessionArrayAdapter(
                requireContext(),
                R.layout.list_item_job_session,
                jobSessions
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
    }

    inner class SessionArrayAdapter(
        context: Context,
        layout: Int,
        var jobSessions: List<JobSession>
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
    val updater: (String) -> Unit
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

        updater("${time.hour}:${time.minute}")
    }
}

class DatePickerFragment(
    val updater: (String) -> Unit
) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
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

        updater(
            "${
                date.month.getDisplayName(
                    TextStyle.FULL,
                    Locale.getDefault()
                )
            } ${date.dayOfMonth}, ${date.year} "
        )
    }
}
