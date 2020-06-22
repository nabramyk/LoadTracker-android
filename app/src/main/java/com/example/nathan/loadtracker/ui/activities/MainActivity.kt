package com.example.nathan.loadtracker.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.core.database.LoadTrackerDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.create_session_dialog.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        title = ""

        button_create_session.setOnClickListener { showCreateDialog() }
        button_continue_session.setOnClickListener { startActivity(Intent(this, JobSessionsActivity::class.java)) }
        button_export.setOnClickListener { startActivity(Intent(this, ExportActivity::class.java)) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun showCreateDialog() {
        val layoutInflater = LayoutInflater.from(this@MainActivity)
        val promptView = layoutInflater.inflate(R.layout.create_session_dialog, null)
        val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)

        val alertDialog = alertDialogBuilder
                .setView(promptView)
                .setCancelable(false)
                .setPositiveButton("Create") { _,_ ->
                    if (!TextUtils.isEmpty(promptView.sessionTitleEditText.text)) {
                        LoadTrackerDatabase.addJobSession(promptView.sessionTitleEditText.text.toString())
                        showStartImmediateDialog(promptView.sessionTitleEditText.text.toString())
                    }
                }
                .setNegativeButton("Cancel"
                ) { dialog, _ -> dialog.cancel() }
                .create()

        alertDialog.show()

        promptView.sessionTitleEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 0) {
                    promptView.sessionTitleEditText.error = "Session title must not be empty"
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