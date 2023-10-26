package com.example.nathan.loadtracker.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.nathan.loadtracker.core.database.entities.Load
import com.example.nathan.loadtracker.databinding.FragmentStatisticsBinding
import com.example.nathan.loadtracker.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch
import kotlin.time.Duration

class StatisticsFragment : Fragment() {

    private lateinit var sessionTitle: String
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionTitle = activity?.title?.toString()!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.getLoadsForActiveJobSession().collect { loads ->
                    updateTotalLoadsTracked(loads)
                    updateAverageRunTime(loads)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * TODO
     *  This just doesn't work, could probably be converted to a composable
     */
    private fun updateTotalLoadsTracked(loads: List<Load>) {
        val materials = HashMap<String, Int>()

        for (l in loads) {
            if (!materials.containsKey(l.material)) materials[l.material] = 1
            else if (materials.containsKey(l.material)) materials[l.material] =
                materials[l.material]!! + 1
        }

        var formattedOutput = materials.toString()
        formattedOutput = formattedOutput.replace("=", ": ")
        formattedOutput = formattedOutput.replace(",", "\n")
        formattedOutput = formattedOutput.replace("{", "")
        formattedOutput = formattedOutput.replace("}", "")

        binding.tvTotalLoads.text = formattedOutput
    }

    /**
     * TODO
     *  Handle the situation when loads span multiple days, i.e. do not calculate the
     *  difference from the last of one day and the first load of the next day
     */
    private fun updateAverageRunTime(loads: List<Load>) {
        var averageRunTime: Duration = Duration.ZERO

        val first = loads.drop(1)
        val second = loads.dropLast(1)

        if (first.isNotEmpty()) {
            averageRunTime = first.mapIndexed { index, load ->
                load.timeLoaded - second[index].timeLoaded
            }.reduce { current, next ->
                return@reduce (current + next) / 2
            }
        }

        binding.tvAverageRunTime.text = "$averageRunTime"
    }
}