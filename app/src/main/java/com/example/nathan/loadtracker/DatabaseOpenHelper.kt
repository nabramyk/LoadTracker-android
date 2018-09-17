package com.example.nathan.loadtracker

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.nathan.loadtracker.models.JobSession
import com.example.nathan.loadtracker.models.Load
import org.jetbrains.anko.db.*

class DatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "load_tracker", null, 1) {
    companion object {
        private var instance: DatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseOpenHelper {
            if (instance == null) {
                instance = DatabaseOpenHelper(ctx.applicationContext)
            }
            return instance!!
        }

        const val columnId = "_id"
        const val columnTitle = "title"
        const val columnCreated = "created"
        const val columnDriver = "driver"
        const val columnStartDate = "start_date"
        const val columnClosedDate = "closed_date"
        const val columnTotalLoads = "total_loads"
        const val columnUnitId = "unit_id"
        const val columnMaterial = "material"
        const val columnCompanyName = "company_name"
        const val columnTimeLoaded = "time_loaded"
        const val columnDateLoaded = "date_loaded"
        const val columnModified = "modified"

        const val jobSessionsTable = "job_sessions"
        const val loadsTable = "loads"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(Companion.jobSessionsTable, true,
                columnId to INTEGER + PRIMARY_KEY + UNIQUE,
                columnTitle to TEXT,
                columnStartDate to TEXT,
                columnClosedDate to TEXT,
                columnCreated to TEXT,
                columnTotalLoads to INTEGER)

        db.createTable(Companion.loadsTable, true,
                columnId to INTEGER + PRIMARY_KEY + UNIQUE,
                columnTitle to TEXT,
                columnDriver to TEXT,
                columnUnitId to TEXT,
                columnMaterial to TEXT,
                columnCompanyName to TEXT,
                columnTimeLoaded to TEXT,
                columnDateLoaded to TEXT,
                columnCreated to TEXT,
                columnModified to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(jobSessionsTable, true)
        db.dropTable(loadsTable, true)
    }

    fun addLoad(load : Load) {
        use {
            insert(loadsTable,
                    columnTitle to load.title,
                    columnDriver to load.driver,
                    columnMaterial to load.material,
                    columnCompanyName to load.companyName)
        }
    }

    fun addJobSession(jobSession: String) {
        use {
            insert(jobSessionsTable,
                    columnTitle to jobSession)
        }
    }

    fun getLoads() {

    }

    fun getLoadsForSession(title : String) : List<Load> {
        return use {
            select(DatabaseOpenHelper.loadsTable).whereArgs("(${Companion.columnTitle}  = {jobSessionTitle})", "jobSessionTitle" to title).parseList(classParser())
        }
    }

    fun getJobSessions() : List<JobSession> {
        return use {
            select(DatabaseOpenHelper.jobSessionsTable).parseList(classParser())
        }
    }

    fun deleteJobSession() {

    }
}

// Access property for Context
val Context.database: DatabaseOpenHelper
    get() = DatabaseOpenHelper.getInstance(applicationContext)