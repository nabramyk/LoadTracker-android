package com.example.nathan.loadtracker.core.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import java.io.File
import java.io.FileWriter

class LoadTrackerCSVExporter {
    companion object {
        fun export(context: Context, jobSessionWithLoads: JobSessionWithLoads,): File {
            val file = File(context.cacheDir, "output.csv")

            FileWriter(file).apply {
                // Converting the string to CharArrays is so far the only way I've figured out
                // to get the entries to properly newline in the .csv
                write("Id, Material, Driver, Title\n".toCharArray())

                jobSessionWithLoads.loads.forEach { load ->
                    write(("${load.id}, " +
                            "${load.material}, " +
                            "${load.driver}, " +
                            "${jobSessionWithLoads.jobSession.jobTitle}\n"
                            ).toCharArray())
                }

                close()
            }

            return file
        }
    }
}