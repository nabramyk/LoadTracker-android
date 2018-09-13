package com.example.nathan.loadtracker.activities

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.nathan.loadtracker.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private var name: EditText? = null
    private var company: EditText? = null

    private var sharedPrefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(settings_activity_toolbar)
        title = "Settings"

        val ab = supportActionBar
        ab!!.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_arrow_back_black_36px))
        ab.setDisplayHomeAsUpEnabled(true)

        sharedPrefs = getSharedPreferences("com.example.nathan.loadtracker", Context.MODE_PRIVATE)

        if (sharedPrefs!!.contains("name")) {
            name?.setText(sharedPrefs!!.getString("name", ""))
        }

        if (sharedPrefs!!.contains("company")) {
            company?.setText(sharedPrefs!!.getString("company", ""))
        }

        saveButton.setOnClickListener {
            sharedPrefs!!.edit().putString("name", name!!.text.toString()).apply()
            sharedPrefs!!.edit().putString("company", company!!.text.toString()).apply()
            finish()
        }
    }
}
