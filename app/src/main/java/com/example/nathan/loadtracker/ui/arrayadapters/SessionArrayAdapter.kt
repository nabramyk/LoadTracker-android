package com.example.nathan.loadtracker.ui.arrayadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.core.database.entities.JobSession

class SessionArrayAdapter(
    context: Context,
    layout: Int,
    var jobSessions: List<JobSession>,
    var onItemSelectedAction: (Long) -> Unit
) : ArrayAdapter<JobSession>(context, layout, jobSessions), AdapterView.OnItemSelectedListener {

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
        onItemSelectedAction(jobSessions[position].id)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}