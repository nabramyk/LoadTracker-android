package com.example.nathan.loadtracker.ui.arrayadapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.example.nathan.loadtracker.core.database.entities.Load
import com.example.nathan.loadtracker.databinding.CellLoadBinding

class TrackingHistoryAdapter(var loads: List<Load>) : RecyclerView.Adapter<TrackingHistoryAdapter.LoadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackingHistoryAdapter.LoadViewHolder {
        val binding = CellLoadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return loads.size
    }

    override fun onBindViewHolder(holder: LoadViewHolder, position: Int) {
        holder.bindViewHolder(loads[position], position)
    }

    inner class LoadViewHolder(val binding: CellLoadBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindViewHolder(load: Load, position: Int) {
            binding.tvCounter.text = (position + 1).toString()
            binding.tvUnitId.text = "Unit ID: " + load.unitId
            binding.tvMaterial.text = "Material: " + load.material
            binding.tvTimeLoaded.text = "Time Loaded: " + load.timeLoaded
        }
    }
}
