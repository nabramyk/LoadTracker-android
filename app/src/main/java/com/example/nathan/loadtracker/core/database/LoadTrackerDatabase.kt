package com.example.nathan.loadtracker.core.database

import android.content.Context
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSession_
import com.example.nathan.loadtracker.core.database.entities.Load
import com.example.nathan.loadtracker.core.database.entities.MyObjectBox
import io.objectbox.Box
import io.objectbox.BoxStore

object LoadTrackerDatabase {

    private lateinit var boxStore: BoxStore

    private val loadBox: Box<Load> by lazy { boxStore.boxFor(Load::class.java) }
    private val jobSessionBox: Box<JobSession> by lazy { boxStore.boxFor(JobSession::class.java) }

    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.applicationContext)
                .build()
    }

    fun getJobSessions(): List<JobSession> = jobSessionBox.all
    fun getJobSession(title: String): JobSession = jobSessionBox.query().equal(JobSession_.jobTitle, title).build().findFirst() ?: JobSession()
    fun getLoadsForSession(jobTitle: String): List<Load> = jobSessionBox.query().equal(JobSession_.jobTitle, jobTitle).build().findFirst()?.loads?.toList() ?: emptyList()
    fun deleteJobSession() {

    }
    fun addJobSession(title: String) = jobSessionBox.put(JobSession(jobTitle = title))
}