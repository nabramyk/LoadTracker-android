package com.example.nathan.loadtracker.activities

import android.content.Intent
import android.os.AsyncTask
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.database
import com.opencsv.CSVWriter
import java.io.File
import com.opencsv.CSVWriter.*

import kotlinx.android.synthetic.main.activity_export.*
import java.io.FileWriter

class ExportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_export)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = "Export"

        val jobs = database.getJobSessions()

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
        for (js in jobs) {
            adapter.add(js.jobTitle.toString())
        }

        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {}

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        bExport.setOnClickListener {
            val loads = database.getLoadsForSession(spinner.selectedItem.toString())
            val file = File(getExternalFilesDir(null), "output.csv")
            val csvWriter = CSVWriter(FileWriter(file))

            loads.forEach {
                val arrStr = arrayOf(it.id.toString(), it.material, it.driver, it.title)
                csvWriter.writeNext(arrStr)
            }

            csvWriter.close()

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(applicationContext, getString(R.string.file_provider_authority), file.absoluteFile))

            startActivityForResult(Intent.createChooser(intent, "Send email...."), 1)
            finish()
        }
    }

    private inner class ExportToCSVTask : AsyncTask<String, String, Boolean>() {

        internal var memoryErr = false

        override fun onPostExecute(result: Boolean?) {
            //val file = File(getExternalFilesDir(null), "output.csv")
            //val path = FileProvider.getUriForFile(applicationContext, getString(R.string.file_provider_authority), file.absoluteFile)

            //val intent = Intent(Intent.ACTION_SEND)
            //intent.type = "text/plain"
            //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            //intent.putExtra(Intent.EXTRA_STREAM, path)

            //startActivityForResult(Intent.createChooser(intent, "Send email...."), 1)
            //finish();
        }

        override fun doInBackground(vararg strings: String): Boolean? {
            //val loads = db.getAllLoadsForSession(spinner.selectedItem.toString())
            //val file = File(getExternalFilesDir(null), "output.csv")

            return true
        }
    }
}
