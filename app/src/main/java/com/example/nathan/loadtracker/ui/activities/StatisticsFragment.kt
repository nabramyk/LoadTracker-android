package com.example.nathan.loadtracker.ui.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nathan.loadtracker.core.database.LoadTrackerDatabase
import com.example.nathan.loadtracker.core.database.entities.Load
import com.example.nathan.loadtracker.databinding.FragmentStatisticsBinding
import java.util.HashMap

class StatisticsFragment : Fragment() {

    private lateinit var sessionTitle: String
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionTitle = activity?.title?.toString()!!
    }

    override fun onStart() {
        super.onStart()

        val loads = LoadTrackerDatabase.getLoadsForSession(sessionTitle)

        updateTotalLoadsTracked(loads)
        updateAverageRunTime(loads)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun updateTotalLoadsTracked(loads: List<Load>) {
        val materials = HashMap<String, Int>()

        for (l in loads) {
            if (!materials.containsKey(l.material)) materials[l.material] = 1
            else if (materials.containsKey(l.material)) materials[l.material] = materials[l.material]!! + 1
        }

        var formattedOutput = materials.toString()
        formattedOutput = formattedOutput.replace("=", ": ")
        formattedOutput = formattedOutput.replace(",", "\n")
        formattedOutput = formattedOutput.replace("{", "")
        formattedOutput = formattedOutput.replace("}", "")

        binding.tvTotalLoads.text = formattedOutput
    }

    private fun updateAverageRunTime(loads: List<Load>) {
        if (loads.isEmpty()) {
            binding.tvAverageRunTime.text = "00:00:00.000"
            return
        }
        var hours = 0
        var minutes = 0
        var seconds = 0
        var milliseconds = 0
        for (l in loads) {
            var timeLoaded = l.timeLoaded
            timeLoaded = timeLoaded.replace(":", " ")
            timeLoaded = timeLoaded.replace(".", " ")
            val components = timeLoaded.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            hours += Integer.parseInt(components[0])
            minutes += Integer.parseInt(components[1])
            seconds += Integer.parseInt(components[2])
            milliseconds += Integer.parseInt(components[3])
        }
        hours /= loads.size
        minutes /= loads.size
        seconds /= loads.size
        milliseconds /= loads.size

        binding.tvAverageRunTime.text = hours.toString() + ":" + minutes + ":" + seconds + "." + milliseconds
    }
}