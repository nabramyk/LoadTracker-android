package com.example.nathan.loadtracker.core.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.format.TextStyle
import java.util.Locale

class DatePickerFragment(
    val updater: (LocalDate) -> Boolean,
    val onError: () -> Unit
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
        //The DatePicker numbers months from 1-12
        if(!updater(LocalDate(year = year, month = Month(month + 1), dayOfMonth = dayOfMonth))) {
            onError()
        }
    }
}