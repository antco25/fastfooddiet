package com.example.fastfooddiet.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.fastfooddiet.R
import com.example.fastfooddiet.viewmodels.SharedViewModel
import java.lang.NumberFormatException
import kotlin.math.roundToInt

class SettingsNumberDialog : DialogFragment() {

    //**** PROPERTIES ****
    private val sharedViewModel : SharedViewModel by navGraphViewModels(R.id.nav_settings)
    private val args : SettingsNumberDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val view = it.layoutInflater.inflate(R.layout.dialog_number_settings, null)
            val editText = view.findViewById<EditText>(R.id.dialog_settings_editText).apply {
                if (args.isValueFloat) {
                    hint = args.defaultValue.toString()
                    inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
                } else
                    hint = args.defaultValue.toInt().toString()
            }

            val builder = AlertDialog.Builder(it)
            builder.setTitle(args.title)
                .setView(view)
                .setPositiveButton("Ok") { _, _ ->
                        stringToFloat(editText.text.toString())?.let { value ->
                            sharedViewModel.apply {
                                settingsHandled = false
                                numberSetting.value = Triple(args.key, value, args.isValueFloat)
                            }
                        }
                    }
                .setNegativeButton("Cancel") { _, _ -> }

            builder.create().also {
                //Show keyboard when opened
                it.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            }
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    //**** METHODS ****
    private fun stringToFloat(string : String) : Float? {
        return try {
            val value = string.toFloat()

            //Round value to 1 decimal
            (value * 10).roundToInt() / 10f
        }
        catch (e : NumberFormatException) { return null }
    }
}