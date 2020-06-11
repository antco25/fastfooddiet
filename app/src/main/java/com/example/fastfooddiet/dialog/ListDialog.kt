package com.example.fastfooddiet.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.fastfooddiet.viewmodels.SharedViewModel

class ListDialog : DialogFragment() {

    //**** PROPERTIES ****
    private val args : ListDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        //Get ViewModel
        val sharedViewModel : SharedViewModel by navGraphViewModels(args.viewModelGraphID)

        return activity?.let {

            val builder = AlertDialog.Builder(it)
            builder.setTitle(args.title)
                .setItems(args.items) { dialog, index ->
                    val pair = Pair(args.items[index],
                        args.itemIds?.get(index))

                    sharedViewModel.apply {
                        listSelectionHandled = false
                        listSelection.value = pair
                    }
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    //**** METHODS ****
}