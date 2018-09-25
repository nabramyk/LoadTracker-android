package com.example.nathan.loadtracker.activities

import android.content.Intent
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.nathan.loadtracker.DatabaseOpenHelper
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.database
import com.example.nathan.loadtracker.models.Load
import com.opencsv.CSVWriter
import java.io.File

import kotlinx.android.synthetic.main.activity_export.*
import java.io.FileWriter

class ExportActivity : AppCompatActivity() {

    private lateinit var loads : List<Load>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_export)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = "Export"

        initView()
    }

    private fun initView() {
        val jobs = database.getJobSessions()

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
        for (js in jobs) {
            adapter.add(js.jobTitle.toString())
        }

        sSession.adapter = adapter
        sSession.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                populateFromJob()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        bExport.setOnClickListener {
            val file = File(getExternalFilesDir(null), "output.csv")
            val csvWriter = CSVWriter(FileWriter(file))

            csvWriter.writeNext(arrayOf(DatabaseOpenHelper.columnId, DatabaseOpenHelper.columnMaterial, DatabaseOpenHelper.columnDriver, DatabaseOpenHelper.columnTitle))
            for (load in loads) {
                csvWriter.writeNext(arrayOf(load.id.toString(), load.material, load.driver, load.title))
            }

            csvWriter.close()

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(applicationContext, getString(R.string.file_provider_authority), file.absoluteFile))

            startActivityForResult(Intent.createChooser(intent, "Send email...."), 1)
            finish()
        }

        cbCurrentDate.setOnClickListener {
            sStartDate.isEnabled = !cbCurrentDate.isChecked
            sEndDate.isEnabled = !cbCurrentDate.isChecked
            sStartTime.isEnabled = !cbCurrentDate.isChecked
            sEndTime.isEnabled = !cbCurrentDate.isChecked
        }
    }

    private fun populateFromJob() {
        loads = database.getLoadsForSession(sSession.selectedItem.toString())

        val dateAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
        loads.asSequence().map { it.dateLoaded }.distinct().toList().forEach { dateAdapter.add(it) }

        sStartDate.adapter = dateAdapter
        sEndDate.adapter = dateAdapter

        val timeAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
        loads.asSequence().map { it.timeLoaded }.distinct().toList().forEach { timeAdapter.add(it) }

        sStartTime.adapter = timeAdapter
        sEndTime.adapter = timeAdapter
    }
}
