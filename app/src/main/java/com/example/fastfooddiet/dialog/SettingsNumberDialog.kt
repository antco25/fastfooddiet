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
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.fastfooddiet.R
import com.example.fastfooddiet.viewmodels.CustomSearchViewModel
import com.example.fastfooddiet.viewmodels.SettingsViewModel
import com.example.fastfooddiet.viewmodels.SharedViewModel
import java.lang.NumberFormatException
import kotlin.math.max

class SettingsNumberDialog : DialogFragment() {

    //**** PROPERTIES ****
    private val sharedViewModel : SharedViewModel by navGraphViewModels(R.id.nav_graph) //TODO: Change to inner nav graph
    private val args : SettingsNumberDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d("xfast", "OnCreateDialog")
        return activity?.let {

            val view = it.layoutInflater.inflate(R.layout.dialog_number_settings, null)
            val editText = view.findViewById<EditText>(R.id.dialog_settings_editText).apply {
                hint = args.defaultValue.toString()
            }

            val builder = AlertDialog.Builder(it)
            builder.setTitle(args.title)
                .setView(view)
                .setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, id ->
                        stringToInt(editText.text.toString())?.let { value ->
                            sharedViewModel.numberPair.value = Pair(args.key, value)
                        }
                    })
                .setNegativeButton("Cancel") { dialog, id -> }

            builder.create().also {
                //Show keyboard when opened
                it.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            }
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    //**** METHODS ****
    private fun stringToInt(string : String) : Int? {
        return try { string.toInt() }
        catch (e : NumberFormatException) { return null }
    }
}