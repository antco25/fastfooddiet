package com.example.fastfooddiet.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.fastfooddiet.R
import com.example.fastfooddiet.data.MealData
import com.example.fastfooddiet.viewmodels.SharedViewModel

class MealDialog : DialogFragment() {

    //**** PROPERTIES ****
    private val args: MealDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        //Get ViewModel
        val sharedViewModel: SharedViewModel by navGraphViewModels(args.viewModelGraphID)

        return activity?.let {
            //Create dialog
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Add to Meal")
                .setItems(args.mealNames) { _, index ->
                    sharedViewModel.mealDataChanged = true
                    sharedViewModel.mealData.value = MealData(args.mealIds[index],
                        args.mealNames[index])
                }.setPositiveButton("Create New Meal") { _, _ ->
                    sharedViewModel.isAddMeal = true
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}


/*
            //Custom adapter and title to control look
            val adapter = ArrayAdapter<String>(it, R.layout.dialog_list_item,
                args.mealNames)

            val title = (it.layoutInflater.inflate(R.layout.dialog_title, null)
                    as TextView).apply { text = "Add to Meal" }
 */