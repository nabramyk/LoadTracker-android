package com.example.nathan.loadtracker.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.fragment_tracking_history.*

import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.arrayadapters.TrackingHistoryAdapter
import com.example.nathan.loadtracker.database
import com.example.nathan.loadtracker.models.Load

class TrackingHistoryFragment : Fragment() {

    private lateinit var sessionTitle: String
    private lateinit var loads: ArrayList<Load>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionTitle = activity?.title?.toString()!!
        loads = context?.database?.getLoadsForSession(sessionTitle) as ArrayList<Load>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tracking_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listAdapter = TrackingHistoryAdapter(context!!, loads)
        trackedLoadHistory.layoutManager = LinearLayoutManager(context)
        trackedLoadHistory.adapter = listAdapter
    }
}