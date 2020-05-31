package com.example.fastfooddiet.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.text.set
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.fastfooddiet.R
import com.example.fastfooddiet.data.MealData
import com.example.fastfooddiet.view.DetailFragmentDirections
import com.example.fastfooddiet.viewmodels.CustomSearchViewModel
import com.example.fastfooddiet.viewmodels.SettingsViewModel
import com.example.fastfooddiet.viewmodels.SharedViewModel
import java.lang.NumberFormatException
import kotlin.math.max

class MealDialog : DialogFragment() {

    //**** PROPERTIES ****
    private val args: MealDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        //Get ViewModel
        val sharedViewModel: SharedViewModel by navGraphViewModels(args.viewModelGraphID)

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Add to Meal")
                .setItems(args.items) { dialog, index ->
                    sharedViewModel.mealDataChanged = true
                    sharedViewModel.mealData.value = MealData(index, args.items[index])
                }
                .setPositiveButton("Add New Meal") { dialog, id ->
                    sharedViewModel.isAddMeal = true
                }
                .setNegativeButton("Cancel") { _, _ -> }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    //**** METHODS ****
}