package com.example.nathan.loadtracker.ui.arrayadapters

import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.ui.activities.TrackingActivity

import com.example.nathan.loadtracker.core.database.entities.JobSession
import kotlinx.android.synthetic.main.cell_job_session.view.*
import java.lang.ref.WeakReference

class JobSessionAdapter(context: Context,
                        jobSessions: ArrayList<JobSession>) : RecyclerView.Adapter<JobSessionAdapter.JobSessionViewHolder>() {

    private val context = WeakReference(context)
    private val jobSessions = jobSessions

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobSessionViewHolder {
        return JobSessionViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return jobSessions.size
    }

    override fun onBindViewHolder(holder: JobSessionViewHolder, position: Int) {
        holder.bindViewHolder(jobSessions[position])
    }

    inner class JobSessionViewHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_job_session, parent, false)) {

        fun bindViewHolder(jobSession: JobSession) {
            itemView.tvTitle.text = jobSession.jobTitle

            itemView.setOnClickListener {
                context.get()?.let { c ->
                    c.startActivity(Intent(c, TrackingActivity::class.java)
                            .putExtra("session_title_index", jobSession.jobTitle), ActivityOptionsCompat.makeBasic().toBundle())
                }
            }
        }
    }
}
