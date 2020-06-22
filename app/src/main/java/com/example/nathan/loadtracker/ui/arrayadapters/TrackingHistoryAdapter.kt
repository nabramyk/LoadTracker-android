package com.example.nathan.loadtracker.ui.arrayadapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.nathan.loadtracker.R

import com.example.nathan.loadtracker.core.database.entities.Load
import kotlinx.android.synthetic.main.cell_load.view.*
import java.lang.ref.WeakReference

class TrackingHistoryAdapter(context: Context,
                             loads: ArrayList<Load>) : RecyclerView.Adapter<TrackingHistoryAdapter.LoadViewHolder>() {

    private val context = WeakReference(context)
    private val loads = loads

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