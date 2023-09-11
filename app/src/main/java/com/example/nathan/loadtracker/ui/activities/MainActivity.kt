package com.example.nathan.loadtracker.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Menu
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.core.database.LoadTrackerDatabase
import com.example.nathan.loadtracker.core.database.entities.JobSession
import com.example.nathan.loadtracker.databinding.ActivityMainBinding
import com.example.nathan.loadtracker.databinding.CreateSessionDialogBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val db: LoadTrackerDatabase by lazy { LoadTrackerDatabase.getInstance(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        title = ""

        binding.buttonCreateSession.setOnClickListener { showCreateDialog() }
        binding.buttonContinueSession.setOnClickListener { startActivity(Intent(this, JobSessionsActivity::class.java)) }
        binding.buttonExport.setOnClickListener { startActivity(Intent(this, ExportActivity::class.java)) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun showCreateDialog() {
        val dialogBinding = CreateSessionDialogBinding.inflate(layoutInflater)

        val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)

        val alertDialog = alertDialogBuilder
                .setView(dialogBinding.root)
                .setCancelable(false)
                .setPositiveButton("Create") { _,_ ->
                    if (!TextUtils.isEmpty(dialogBinding.sessionTitleEditText.text)) {
                        db.jobSessionDao().addNewJobSession(
                            JobSession(
                                jobTitle = dialogBinding.sessionTitleEditText.text.toString()
                            )
                        )
                        showStartImmediateDialog(dialogBinding.sessionTitleEditText.text.toString())
                    }
                }
                .setNegativeButton("Cancel"
                ) { dialog, _ -> dialog.cancel() }
                .create()

        alertDialog.show()

        dialogBinding.sessionTitleEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 0) {
                    dialogBinding.sessionTitleEditText.error = "Session title must not be empty"
                }
            }
        })
    }

    private fun showStartImmediateDialog(title: String) {
        AlertDialog
                .Builder(this@MainActivity)
                .setMessage("Start this session now?")
                .setPositiveButton("Yes") { _, _ ->
                    startActivity(Intent(this, TrackingActivity::class.java)
                            .putExtra("session_title_index", title))
                }
                .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
                .create()
                .show()
    }
}