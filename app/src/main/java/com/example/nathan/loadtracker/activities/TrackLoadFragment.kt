package com.example.nathan.loadtracker.activities

import android.content.Context
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nathan.loadtracker.R
import com.example.nathan.loadtracker.database
import com.example.nathan.loadtracker.models.Load
import kotlinx.android.synthetic.main.fragment_load_tracking.*
import java.text.SimpleDateFormat
import java.util.*

class TrackLoadFragment : Fragment() {

    private lateinit var sessionTitle: String
    private lateinit var js: MutableList<Load>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionTitle = activity?.title?.toString()!!

        js = context?.database?.getLoadsForSession(sessionTitle)!!.toMutableList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_load_tracking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bTrack.setOnClickListener {
            if (materialInput.text.toString().trim { it <= ' ' }.isEmpty()) {
                Snackbar.make(view, "Missing material", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (unitIDInput.text.toString().trim { it <= ' ' }.isEmpty()) {
                Snackbar.make(view, "Missing unit ID", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (driverNameInput.text.toString().trim { it <= ' ' }.isEmpty()) {
                Snackbar.make(view, "Missing driver name", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (companyNameInput.text.toString().trim { it <= ' ' }.isEmpty()) {
                Snackbar.make(view, "Missing company name", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val c = Calendar.getInstance()
            context?.database?.addLoad(
                    Load(
                            id = null,
                            title = sessionTitle,
                            driver = driverNameInput.text.toString(),
                            unitId = unitIDInput.text.toString(),
                            material = materialInput.text.toString(),
                            timeLoaded = SimpleDateFormat("HH:mm:ss.SSS").format(c.time),
                            dateLoaded = SimpleDateFormat("yyyy/MM/dd").format(c.time),
                            created = SimpleDateFormat("yyyy/MM/dd").format(c.time),
                            modified = null,
                            companyName = companyNameInput.text.toString()
                    )
            )

            Snackbar.make(view, "Tracked!", Snackbar.LENGTH_LONG).show()
        }

        if (js.isNotEmpty()) {
            materialInput.setText(js[js.size - 1].material)
            unitIDInput.setText(js[js.size - 1].unitId)
            driverNameInput.setText(js[js.size - 1].driver)
            companyNameInput.setText(js[js.size - 1].companyName)
        } else {
            val sharedPrefs = activity?.getSharedPreferences("com.example.nathan.loadtracker", Context.MODE_PRIVATE)
            driverNameInput.setText(sharedPrefs?.getString("name", ""))
            companyNameInput.setText(sharedPrefs?.getString("company", ""))
        }
    }
}