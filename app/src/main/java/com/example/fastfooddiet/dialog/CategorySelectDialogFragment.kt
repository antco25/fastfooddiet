package com.example.fastfooddiet.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.fastfooddiet.R
import com.example.fastfooddiet.viewmodels.CustomSearchViewModel

class CategorySelectDialogFragment : DialogFragment() {

    //**** PROPERTIES ****
    private val customSearchViewModel : CustomSearchViewModel by navGraphViewModels(R.id.nav_custom_search)
    private val args : CategorySelectDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let { it ->
            val title = if (args.isRestaurant) "Restaurants" else "Food Categories"

            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
                .setMultiChoiceItems(args.items, args.checkedItems) { dialog, index, isChecked ->

                        //Note: Interface is automatically updating values
                        // in 'args.checkedItems' array so no need to update directly

                        val alertDialog = dialog as AlertDialog

                        //Index 0 refers to "All". Check or uncheck all items if it is selected
                        if (index == 0) {
                            setAllItemChecked(isChecked, alertDialog, args.checkedItems)
                        }

                        //If any other item is unchecked, "All" must be unchecked
                        else if (!isChecked) {
                            args.checkedItems[0] = false
                            alertDialog.listView.setItemChecked(0, false)
                        }

                        //If all items are checked, "All" must be checked
                        else {
                            //Check that the last 'false' value is index 0
                            if (args.checkedItems.indexOfLast { !it } == 0) {
                                args.checkedItems[0] = true
                                alertDialog.listView.setItemChecked(0, true)
                            }
                        }

                }
                .setPositiveButton("Ok") { _, _ ->
                        customSearchViewModel.apply {
                            val checkedItems = checkForEmptySelection()
                            if (args.isRestaurant)
                                updateCheckedRestaurants(checkedItems)
                            else
                                updateCheckedFoodTypes(checkedItems)
                        }
                    }
                .setNegativeButton("Cancel") { _, _ ->
                        // User cancelled the dialog
                    }

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

    private fun checkForEmptySelection() : BooleanArray {

        args.checkedItems.indices.forEach {index ->
            if (args.checkedItems[index])
                return args.checkedItems
        }

        return BooleanArray(args.checkedItems.size) {true}
    }

}