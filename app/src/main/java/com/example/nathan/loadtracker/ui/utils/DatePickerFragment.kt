package com.example.nathan.loadtracker.ui.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.format.TextStyle
import java.util.Locale

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