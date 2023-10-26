package com.example.nathan.loadtracker.ui.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import java.io.File
import java.io.FileWriter

class CsvExporter {
    companion object {
        fun export(context: Context, jobSessionWithLoads: JobSessionWithLoads,) {
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

                // I originally was using `createChooser` but it was throwing some error messages
                // about file permissions. This could be cleaner but it works for the time being.
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    putExtra(
                        Intent.EXTRA_STREAM,
                        FileProvider.getUriForFile(
                            context,
                            context.getString(R.string.file_provider_authority),
                            file
                        )
                    )
                }
                context.startActivity(intent)
            }
        }
    }
}