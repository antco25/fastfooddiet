package com.example.fastfooddiet.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.fastfooddiet.data.MealData
import com.example.fastfooddiet.viewmodels.SharedViewModel

class MealDialog : DialogFragment() {

    //**** PROPERTIES ****
    private val args: MealDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        //Get ViewModel
        val sharedViewModel: SharedViewModel by navGraphViewModels(args.viewModelGraphID)

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Add to Meal")
                .setItems(args.mealNames) { _, index ->

                    sharedViewModel.mealDataChanged = true
                    sharedViewModel.mealData.value = MealData(args.mealIds[index],
                        args.mealNames[index])

                }
                .setPositiveButton("Add New Meal") { _, _ ->
                    sharedViewModel.isAddMeal = true
                }
                .setNegativeButton("Cancel") { _, _ -> }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}