package com.example.nathan.loadtracker.ui.activities

import android.content.Intent
import androidx.core.content.FileProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.core.database.LoadTrackerDatabase
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.core.database.entities.JobSessionWithLoads
import com.example.nathan.loadtracker.databinding.ActivityExportBinding
import com.example.nathan.loadtracker.ui.viewmodels.TrackingViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter

class ExportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExportBinding
    private val viewModel: TrackingViewModel by lazy {
        ViewModelProvider(this, TrackingViewModel.Factory(application))[TrackingViewModel::class.java]
    }

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
        viewModel.allJobSessionsWithLoads.observe(this) { jobSessionsWithLoads ->
            val jobs = jobSessionsWithLoads.map { it.jobSession }

            val adapter = ArrayAdapter<Pair<String, Long>>(this, android.R.layout.simple_spinner_dropdown_item)
            for (js in jobs) {
                adapter.add(Pair(js.jobTitle.toString(), js.id))
            }
            binding.sSession.adapter = adapter

            binding.sSession.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                    populateFromJob(jobSessionsWithLoads[i])
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {}
            }
        }

        binding.bExport.setOnClickListener {
            val file = File(cacheDir, "output.csv")
            FileWriter(file).apply {
                // Converting the string to CharArrays is so far the only way I've figured out
                // to get the entries to properly newline in the .csv
                write("Id, Material, Driver, Title\n".toCharArray())

                viewModel.jobSessionToExport!!.loads.forEach { load ->
                    write("${load.id}, ${load.material}, ${load.driver}, ${viewModel.jobSessionToExport!!.jobSession.jobTitle}\n".toCharArray())
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
                            applicationContext,
                            getString(R.string.file_provider_authority),
                            file
                        ))
                }
                startActivity(intent)
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

    private fun populateFromJob(jobSessionWithLoads: JobSessionWithLoads) {
        val dateAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
        jobSessionWithLoads.loads.asSequence().map { it.dateLoaded }.distinct().toList().forEach { dateAdapter.add(it) }

        binding.sStartDate.adapter = dateAdapter
        binding.sEndDate.adapter = dateAdapter

        val timeAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
        jobSessionWithLoads.loads.asSequence().map { it.timeLoaded }.distinct().toList().forEach { timeAdapter.add(it) }

        binding.sStartTime.adapter = timeAdapter
        binding.sEndTime.adapter = timeAdapter
    }
}
