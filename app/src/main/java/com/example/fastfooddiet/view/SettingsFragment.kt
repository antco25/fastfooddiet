package com.example.fastfooddiet.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.fastfooddiet.R
import com.example.fastfooddiet.databinding.FragmentSettingsBinding
import com.example.fastfooddiet.viewcomponent.CustomButton
import com.example.fastfooddiet.viewmodels.SettingsViewModel
import com.example.fastfooddiet.viewmodels.SharedViewModel

class SettingsFragment : Fragment() {

    //**** PROPERTIES ****
    private lateinit var settingsViewModel: SettingsViewModel
    private val sharedViewModel : SharedViewModel by navGraphViewModels(R.id.nav_settings)

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Get ViewModel
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java).apply {
            isCustomData.value = isCustomData(context)
        }

        setupSharedViewModel()

        val binding = FragmentSettingsBinding.inflate(inflater, container, false)
            .apply {
                viewmodel = settingsViewModel
                fragment = this@SettingsFragment
                lifecycleOwner = viewLifecycleOwner
            }

        return binding.root
    }

    //**** METHODS ****
    fun onNutritionButtonClick(isCustomData : Boolean) {
        settingsViewModel.isCustomData.value = isCustomData

        context?.apply {
            val sharedPref = getSharedPreferences(
                resources.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE)

            val value = if (isCustomData) 1 else 0

            with (sharedPref.edit()) {
                putInt(getString(R.string.nutrition_key), value)
                commit()
            }
        }
    }

    fun onNutritionValueClick(key : String, defaultValue : Int, name : String) {
        if (settingsViewModel.isCustomData.value != true)
            return

        val header = "Set $name Limit"

        //Dialog accepts only float, so ints must be converted
        val action = SettingsFragmentDirections
            .toSettingsNumberDialog(key, defaultValue.toFloat(), header, isValueFloat = false)
        findNavController().navigate(action)
    }

    fun onNutritionValueClick(key : String, defaultValue : Float, name : String) {
        if (settingsViewModel.isCustomData.value != true)
            return

        val header = "Set $name Limit"
        val action = SettingsFragmentDirections
            .toSettingsNumberDialog(key, defaultValue, header, isValueFloat = true)
        findNavController().navigate(action)
    }

    fun toAboutDialog() {
        val action = SettingsFragmentDirections.toAboutDialog()
        findNavController().navigate(action)
    }

    private fun isCustomData(context : Context?) : Boolean {
        context?.apply {
            val sharedPref = getSharedPreferences(
                resources.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE)

            val value = sharedPref.getInt(resources
                .getString(R.string.nutrition_key), 0)
            return (value == 1)
        }
        return false
    }

    private fun setupSharedViewModel() {
        sharedViewModel.numberSetting.observe(viewLifecycleOwner, Observer { triple ->

            if (sharedViewModel.settingsHandled)
                return@Observer

            sharedViewModel.settingsHandled = true

            val key = triple.first
            val value = triple.second
            val isFloat = triple.third

            //Check if submitted value is acceptable
            if (value > 9999 || value <= 0) {
                val text = "Invalid value"
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            } else {
                //Update database
                val nutrition = settingsViewModel.nutritionData.value
                nutrition?.let {

                    if (isFloat)
                        nutrition.updateData(key, value)
                    else
                        nutrition.updateData(key, value.toInt())

                    settingsViewModel.updateCustomData(nutrition)
                    val text = "Value Updated"
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                }
            }

        })
    }
}