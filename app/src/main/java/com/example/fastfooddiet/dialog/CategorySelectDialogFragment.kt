package com.example.fastfooddiet.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.fastfooddiet.R
import com.example.fastfooddiet.viewmodels.CustomSearchViewModel
import com.example.fastfooddiet.viewmodels.SharedViewModel

class CategorySelectDialogFragment : DialogFragment() {

    //**** PROPERTIES ****
    private val customSearchViewModel : CustomSearchViewModel by navGraphViewModels(R.id.nav_graph) //TODO: Change to inner nav graph
    private val args : CategorySelectDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val title = if (args.isRestaurant) "Restaurants" else "Food Categories"

            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
                .setMultiChoiceItems(args.items, args.checkedItems,
                    DialogInterface.OnMultiChoiceClickListener { dialog, index, isChecked ->

                        //Note: Interface is automatically updating values
                        // in 'args.checkedItems' array

                        val alertDialog = dialog as AlertDialog

                        //Index 0 is always "All". Check or uncheck all items if it is selected
                        if (index == 0) {
                            setAllItemChecked(isChecked, alertDialog, args.checkedItems)
                        }

                        //If item is unchecked, "All" must be unchecked
                        else if (!isChecked) {
                            args.checkedItems[0] = false
                            alertDialog.listView.setItemChecked(0, false)
                        }

                        //If all items are checked, "All" must be checked
                        else {
                            if (args.checkedItems.indexOfLast { !it } == 0) {
                                args.checkedItems[0] = true
                                alertDialog.listView.setItemChecked(0, true)
                            }
                        }

                })
                .setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, id ->
                        customSearchViewModel.apply {
                            if (args.isRestaurant)
                                updateCheckedRestaurants(args.checkedItems)
                            else
                                updateCheckedFoodTypes(args.checkedItems)
                        }
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setAllItemChecked(isChecked : Boolean, dialog : AlertDialog,
                                  checkedItems : BooleanArray) {
        checkedItems.indices.forEach { index ->
            checkedItems[index] = isChecked
            dialog.listView.setItemChecked(index, isChecked)
        }
    }

}