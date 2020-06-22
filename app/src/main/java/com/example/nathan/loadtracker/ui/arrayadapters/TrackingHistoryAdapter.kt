package com.example.nathan.loadtracker.ui.arrayadapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.nathan.loadtracker.R

import com.example.nathan.loadtracker.core.database.entities.Load
import kotlinx.android.synthetic.main.cell_load.view.*

class TrackingHistoryAdapter(private val loads: List<Load>) : RecyclerView.Adapter<TrackingHistoryAdapter.LoadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackingHistoryAdapter.LoadViewHolder {
        return LoadViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return loads.size
    }

    override fun onBindViewHolder(holder: LoadViewHolder, position: Int) {
        holder.bindViewHolder(loads[position])
    }

    inner class LoadViewHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_load, parent, false)) {
        fun bindViewHolder(load: Load) {
            itemView.tvCounter.text = load.id.toString()
            itemView.tvUnitId.text = "Unit ID: " + load.unitId
            itemView.tvMaterial.text = "Material: " + load.material
            itemView.tvTimeLoaded.text = "Time Loaded: " + load.timeLoaded
        }
    }
}
