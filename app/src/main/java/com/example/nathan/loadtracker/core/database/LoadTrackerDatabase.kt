package com.example.nathan.loadtracker.core.database

import android.content.Context
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSession_
import com.example.nathan.loadtracker.core.database.entities.Load
import com.example.nathan.loadtracker.core.database.entities.MyObjectBox
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor

object LoadTrackerDatabase {

    private lateinit var boxStore: BoxStore
    private val jobSessionBox: Box<JobSession> by lazy { boxStore.boxFor() }
    private val loadBox: Box<Load> by lazy { boxStore.boxFor() }

    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.applicationContext)
                .build()
    }

    fun getBoxStore(): BoxStore = boxStore

    fun getJobSessions(): List<JobSession> = jobSessionBox.all
    fun getJobSession(title: String): JobSession = jobSessionBox.query(JobSession_.jobTitle.equal(title)).build().findFirst() ?: JobSession()
    fun getLoadsForSession(jobTitle: String): List<Load> = jobSessionBox.query(JobSession_.jobTitle.equal(jobTitle)).build().findFirst()?.loads?.toList() ?: emptyList()


    fun addJobSession(title: String) = jobSessionBox.put(JobSession(jobTitle = title))

    fun addLoad(load: Load) = loadBox.put(load)
}