package com.example.nathan.loadtracker.jobsession

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

import com.example.nathan.loadtracker.Item
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.arrayadapters.JobSessionArrayAdapter
import com.example.nathan.loadtracker.models.JobSession

class JobSessionItem(js: JobSession) : Item {

    private val title: String? = js.jobTitle
    private val createdDate: String? = js.created
    private val totalLoads: String = js.totalLoads.toString()

    override fun getViewType(): Int {
        return JobSessionArrayAdapter.RowType.LIST_ITEM.ordinal
    }

    override fun getView(inflater: LayoutInflater, convertView: View?): View {
        val view: View = convertView ?: inflater.inflate(R.layout.jobsessionrow, null)

        val sessionTitle = view.findViewById<View>(R.id.sessionTitle) as TextView
        val created = view.findViewById<View>(R.id.createdTextView) as TextView
        val totalLoads = view.findViewById<View>(R.id.totalLoadsTextView) as TextView

        sessionTitle.text = this.title
        created.text = this.createdDate
        totalLoads.text = this.totalLoads

        return view
    }
}
