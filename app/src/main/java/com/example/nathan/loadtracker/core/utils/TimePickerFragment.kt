package com.example.nathan.loadtracker.core.utils

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import kotlinx.datetime.LocalTime

class TimePickerFragment(
    private val hour: Int,
    private val minute: Int,
    val updater: (LocalTime) -> Unit
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
        updater(LocalTime.parse("${hourOfDay.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"))
    }
}