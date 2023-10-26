package com.example.nathan.loadtracker.ui.utils

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month

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