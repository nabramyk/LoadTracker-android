package com.example.nathan.loadtracker.load

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

import com.example.nathan.loadtracker.Item
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.arrayadapters.TrackingHistoryArrayAdapter
import com.example.nathan.loadtracker.models.Load

class LoadListItem(load: Load) : Item {

    val id: Int = load.id!!
    private val timeLoaded: String?
    private val material: String?
    private val unitId: String?

    init {
        this.timeLoaded = load.timeLoaded
        this.material = load.material
        this.unitId = load.unitId
    }

    override fun getViewType(): Int {
        return TrackingHistoryArrayAdapter.RowType.LIST_ITEM.ordinal
    }

    override fun getView(inflater: LayoutInflater, convertView: View?): View {
        val view: View
        if (convertView == null) {
            view = inflater.inflate(R.layout.trackedloadrow, null)
            // Do some initialization
        } else {
            view = convertView
        }

        val trackedLoads = view.findViewById<View>(R.id.timeLoaded) as TextView
        val material = view.findViewById<View>(R.id.material) as TextView
        val unitId = view.findViewById<View>(R.id.unitId) as TextView

        trackedLoads.text = this.timeLoaded!!
        material.text = this.material!!
        unitId.text = this.unitId!!

        return view
    }
}