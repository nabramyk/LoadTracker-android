package com.example.nathan.loadtracker.ui.activities

import android.content.Intent
import androidx.core.content.FileProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.core.database.LoadTrackerDatabase
import com.example.nathan.loadtracker.core.database.entities.Load
import com.example.nathan.loadtracker.databinding.ActivityExportBinding
import java.io.File
import java.io.FileWriter

class ExportActivity : AppCompatActivity() {

    private lateinit var loads : List<Load>
    private lateinit var binding: ActivityExportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExportBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = "Export"

        initView()
    }

    private fun initView() {
        val jobs = LoadTrackerDatabase.getJobSessions()

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
        for (js in jobs) {
            adapter.add(js.jobTitle.toString())
        }

        binding.sSession.adapter = adapter
        binding.sSession.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                populateFromJob()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        binding.bExport.setOnClickListener {
            val file = File(cacheDir, "output.csv")
            FileWriter(file).apply {
                // Converting the string to CharArrays is so far the only way I've figured out
                // to get the entries to properly newline in the .csv
                write("Id, Material, Driver, Title\n".toCharArray())
                loads.forEach { load ->
                    write("${load.id}, ${load.material}, ${load.driver}, ${load.jobSession.target.jobTitle}\n".toCharArray())
                }

                close()

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.putExtra(
                    Intent.EXTRA_STREAM,
                    FileProvider.getUriForFile(
                        applicationContext,
                        getString(R.string.file_provider_authority),
                        file
                    )
                )

                startActivityForResult(Intent.createChooser(intent, "Send email...."), 1)
                finish()
            }
        }

        binding.cbCurrentDate.setOnClickListener {
            binding.sStartDate.isEnabled = !binding.cbCurrentDate.isChecked
            binding.sEndDate.isEnabled = !binding.cbCurrentDate.isChecked
            binding.sStartTime.isEnabled = !binding.cbCurrentDate.isChecked
            binding.sEndTime.isEnabled = !binding.cbCurrentDate.isChecked
        }
    }

    private fun populateFromJob() {
        loads = LoadTrackerDatabase.getLoadsForSession(binding.sSession.selectedItem.toString())

        val dateAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
        loads.asSequence().map { it.dateLoaded }.distinct().toList().forEach { dateAdapter.add(it) }

        binding.sStartDate.adapter = dateAdapter
        binding.sEndDate.adapter = dateAdapter

        val timeAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
        loads.asSequence().map { it.timeLoaded }.distinct().toList().forEach { timeAdapter.add(it) }

        binding.sStartTime.adapter = timeAdapter
        binding.sEndTime.adapter = timeAdapter
    }
}
