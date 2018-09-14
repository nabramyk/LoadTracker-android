package com.example.nathan.loadtracker.load

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

import com.example.nathan.loadtracker.Item
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.arrayadapters.TrackingHistoryArrayAdapter
import com.example.nathan.loadtracker.models.Load

class LoadListItem(l: Load) : Item {

    private val load = l

    override fun getViewType(): Int {
        return TrackingHistoryArrayAdapter.RowType.LIST_ITEM.ordinal
    }

    override fun getView(inflater: LayoutInflater, convertView: View?): View {
        val view: View = convertView ?: inflater.inflate(R.layout.trackedloadrow, null)

        val trackedLoads = view.findViewById<View>(R.id.timeLoaded) as TextView
        val material = view.findViewById<View>(R.id.material) as TextView
        val unitId = view.findViewById<View>(R.id.unitId) as TextView

        trackedLoads.text = load.timeLoaded
        material.text = load.material
        unitId.text = load.unitId

        return view
    }
}