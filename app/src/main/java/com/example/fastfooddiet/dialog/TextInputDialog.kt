package com.example.fastfooddiet.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.fastfooddiet.R
import com.example.fastfooddiet.viewmodels.SharedViewModel

class TextInputDialog : DialogFragment() {

    //**** PROPERTIES ****
    private val args : TextInputDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        //Get ViewModel
        val sharedViewModel : SharedViewModel by navGraphViewModels(args.viewModelGraphID)

        return activity?.let {

            val view = it.layoutInflater.inflate(R.layout.dialog_text_input, null)
                .apply {  findViewById<TextView>(R.id.dialog_title).text = args.title }
            val editText = view.findViewById<EditText>(R.id.dialog_editText)

            val builder = AlertDialog.Builder(it)
            builder.setView(view)
                .setPositiveButton("Ok") { _, _ ->
                        val text = editText.text.toString()
                        sharedViewModel.textChanged = true
                        sharedViewModel.textInput.value = text
                    }
                .setNegativeButton("Cancel") { _, _ -> }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    //**** METHODS ****
}