package com.example.nathan.loadtracker.ui.arrayadapters

import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.nathan.loadtracker.ui.activities.TrackingActivity

import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.databinding.CellJobSessionBinding
import java.lang.ref.WeakReference

class JobSessionAdapter(context: Context,
                        private val jobSessions: ArrayList<JobSession>
) : RecyclerView.Adapter<JobSessionAdapter.JobSessionViewHolder>() {

    private val context = WeakReference(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobSessionViewHolder {
        val binding = CellJobSessionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobSessionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return jobSessions.size
    }

    override fun onBindViewHolder(holder: JobSessionViewHolder, position: Int) {
        holder.bindViewHolder(jobSessions[position])
    }

    inner class JobSessionViewHolder(private val binding: CellJobSessionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindViewHolder(jobSession: JobSession) {
            binding.tvTitle.text = jobSession.jobTitle

            binding.root.setOnClickListener {
                context.get()?.let { c ->
                    c.startActivity(Intent(c, TrackingActivity::class.java)
                            .putExtra("session_title_index", jobSession.jobTitle), ActivityOptionsCompat.makeBasic().toBundle())
                }
            }
        }
    }
}
