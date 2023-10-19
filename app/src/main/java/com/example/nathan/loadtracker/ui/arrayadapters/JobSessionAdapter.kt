package com.example.nathan.loadtracker.ui.arrayadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.databinding.CellJobSessionBinding

class JobSessionAdapter(
    var jobSessions: ArrayList<Pair<JobSession, Boolean>>,
    private var onItemClicked: ((jobSession: JobSession) -> Unit),
    private var onCloseSession: ((jobSession: JobSession, isActiveSession: Boolean) -> Unit)
) : RecyclerView.Adapter<JobSessionAdapter.JobSessionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobSessionViewHolder {
        val binding =
            CellJobSessionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobSessionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return jobSessions.size
    }

    override fun onBindViewHolder(holder: JobSessionViewHolder, position: Int) {
        holder.bindViewHolder(jobSessions[position].first, jobSessions[position].second)
    }

    inner class JobSessionViewHolder(private val binding: CellJobSessionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindViewHolder(jobSession: JobSession, isActiveSession: Boolean = false) {
            binding.tvJobSessionCurrentlyActiveHeader.isVisible = isActiveSession
            binding.tvTitle.text = jobSession.jobTitle

            binding.apply {
                root.setOnClickListener { onItemClicked(jobSession) }
                binding.bClose.setOnClickListener {
                    onCloseSession(jobSession, isActiveSession)
                }
            }
        }
    }
}
