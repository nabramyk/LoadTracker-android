package com.example.nathan.loadtracker.activities

import android.content.Intent
import android.os.AsyncTask
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.example.nathan.loadtracker.DatabaseHandler
import com.example.nathan.loadtracker.R
import java.io.File
import java.util.ArrayList

import kotlinx.android.synthetic.main.activity_export.*

class ExportActivity : AppCompatActivity() {

    internal lateinit var db: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_export)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = "Export"

        db = DatabaseHandler(this)
        val jobs = db.allJobSessions
        val jobTitles = ArrayList<CharSequence>()
        for (js in jobs) {
            jobTitles.add(js.jobTitle)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {}

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }
    }

    private inner class ExportToCSVTask : AsyncTask<String, String, Boolean>() {

        internal var memoryErr = false


        override fun onPostExecute(result: Boolean?) {
            val file = File(getExternalFilesDir(null), "output.csv")
            val path = FileProvider.getUriForFile(applicationContext, getString(R.string.file_provider_authority), file.absoluteFile)
            Log.d("here", path.path)

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.putExtra(Intent.EXTRA_STREAM, path)

            startActivityForResult(Intent.createChooser(intent, "Send email...."), 1)
            //finish();
        }

        override fun doInBackground(vararg strings: String): Boolean? {
            val loads = db.getAllLoadsForSession(spinner.selectedItem.toString())
            val file = File(getExternalFilesDir(null), "output.csv")

            return true
        }
    }
}
