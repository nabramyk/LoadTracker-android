package com.example.nathan.loadtracker.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import com.example.nathan.loadtracker.DatabaseOpenHelper
import com.example.nathan.loadtracker.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.create_session_dialog.view.*
import org.jetbrains.anko.db.*

class MainActivity : AppCompatActivity() {

    private lateinit var db : DatabaseOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        title = ""

        db = DatabaseOpenHelper.getInstance(this)

        button_create_session.setOnClickListener { showCreateDialog() }
        button_continue_session.setOnClickListener { startActivity(Intent(this, JobSessionsActivity::class.java)) }
        button_continue_last_session.setOnClickListener {
        }
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
                        db.use { insert(DatabaseOpenHelper.jobSessionsTable,
                                DatabaseOpenHelper.columnTitle to promptView.sessionTitleEditText.text.toString()) }
                        //showStartImmediateDialog(js.jobTitle)
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