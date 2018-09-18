package com.example.nathan.loadtracker.activities

import android.content.Context
import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.database
import com.example.nathan.loadtracker.models.Load
import kotlinx.android.synthetic.main.fragment_load_tracking.*

class TrackLoadFragment : Fragment() {

    private lateinit var js: MutableList<Load>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_load_tracking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (js.isNotEmpty()) {
            materialInput.setText(js[js.size - 1].material)
            unitIDInput.setText(js[js.size - 1].unitId)
            driverNameInput.setText(js[js.size - 1].driver)
            companyNameInput.setText(js[js.size - 1].companyName)
        } else {
            val sharedPrefs = activity?.getSharedPreferences("com.example.nathan.loadtracker", Context.MODE_PRIVATE)
            driverNameInput.setText(sharedPrefs?.getString("name", ""))
            companyNameInput.setText(sharedPrefs?.getString("company", ""))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        js = context?.database?.getLoadsForSession(activity?.title?.toString()!!)!!.toMutableList()
    }
}