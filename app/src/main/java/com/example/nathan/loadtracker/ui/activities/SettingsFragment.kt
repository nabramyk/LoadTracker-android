package com.example.nathan.loadtracker.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nathan.loadtracker.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if (sharedPrefs!!.contains("name")) {
//            name?.setText(sharedPrefs!!.getString("name", ""))
//        }
//
//        if (sharedPrefs!!.contains("company")) {
//            company?.setText(sharedPrefs!!.getString("company", ""))
//        }
//
//        binding.saveButton.setOnClickListener {
//            sharedPrefs!!.edit().putString("name", name!!.text.toString()).apply()
//            sharedPrefs!!.edit().putString("company", company!!.text.toString()).apply()
//            finish()
//        }
    }
}
