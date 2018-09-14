package com.example.nathan.loadtracker.jobsession

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

import com.example.nathan.loadtracker.Item
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.arrayadapters.JobSessionArrayAdapter
import com.example.nathan.loadtracker.models.JobSession

class JobSessionItem(js: JobSession) : Item {

    private val title: String?
    private val created_date: String?
    private val totalLoads: String

    init {
        this.title = js.jobTitle
        this.created_date = js.created
        this.totalLoads = js.totalLoads.toString()
    }

    override fun getViewType(): Int {
        return JobSessionArrayAdapter.RowType.LIST_ITEM.ordinal
    }

    override fun getView(inflater: LayoutInflater, convertView: View?): View {
        val view: View
        if (convertView == null) {
            view = inflater.inflate(R.layout.jobsessionrow, null)
            // Do some initialization
        } else {
            view = convertView
        }

        val sessionTitle = view.findViewById<View>(R.id.sessionTitle) as TextView
        val created = view.findViewById<View>(R.id.createdTextView) as TextView
        val totalLoads = view.findViewById<View>(R.id.totalLoadsTextView) as TextView

        sessionTitle.text = this.title
        created.text = this.created_date
        totalLoads.text = this.totalLoads

        return view
    }
}
