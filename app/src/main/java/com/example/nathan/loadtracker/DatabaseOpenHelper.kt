package com.example.nathan.loadtracker

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class DatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "load_tracker", null, 1) {
    companion object {
        const val jobSessionsTable = "job_sessions"
        const val loadsTable = "loads"
        const val columnId = "_id"
        const val columnTitle = "title"
        const val columnCreated = "created"

        private var instance: DatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseOpenHelper {
            if (instance == null) {
                instance = DatabaseOpenHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(jobSessionsTable, true,
                columnId to INTEGER + PRIMARY_KEY + UNIQUE,
                columnTitle to TEXT,
                "start_date" to TEXT,
                "closed_date" to TEXT,
                columnCreated to TEXT,
                "total_loads" to INTEGER)

        db.createTable(loadsTable, true,
                columnId to INTEGER + PRIMARY_KEY + UNIQUE,
                columnTitle to TEXT,
                "driver" to TEXT,
                "unit_id" to TEXT,
                "material" to TEXT,
                "company_name" to TEXT,
                "time_loaded" to TEXT,
                "date_loaded" to TEXT,
                columnCreated to TEXT,
                "modified" to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(jobSessionsTable, true)
        db.dropTable(loadsTable, true)
    }
}

// Access property for Context
val Context.database: DatabaseOpenHelper
    get() = DatabaseOpenHelper.getInstance(applicationContext)