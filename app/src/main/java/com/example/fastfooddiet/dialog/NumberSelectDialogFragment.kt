package com.example.fastfooddiet.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
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
import java.lang.NumberFormatException
import kotlin.math.max

class NumberSelectDialogFragment : DialogFragment() {

    //**** PROPERTIES ****
    private val customSearchViewModel : CustomSearchViewModel by navGraphViewModels(R.id.nav_graph) //TODO: Change to inner nav graph
    private val args: NumberSelectDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val maxValue = customSearchViewModel.getMaxNutritionData(args.maxKey)

            val currentValue = if (!args.isMax)
                customSearchViewModel.getNutritionData(args.minKey)
            else {
                val max = customSearchViewModel.getNutritionData(args.maxKey)
                if (max == -1) maxValue else max
            }

            val view = it.layoutInflater.inflate(R.layout.dialog_number_select, null)
            val valueText = view.findViewById<TextView>(R.id.num_select_dialog_value)
            val seekBar = view.findViewById<SeekBar>(R.id.num_select_dialog_seekBar)

            setViews(valueText, seekBar, currentValue, maxValue, args.isMax)

            val title = if (args.isMax) "Set maximum value" else "Set minimum value"
            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
                .setView(view)
                .setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, id ->
                        customSearchViewModel.updateNutritionData(args.maxKey, args.minKey,
                            args.isMax, convertProgressToValue(seekBar.progress, maxValue))
                    })
                .setNegativeButton("Cancel") { dialog, id -> }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    //**** METHODS ****
    private fun setViews(valueText: TextView, seekBar: SeekBar,
                         currentValue : Int, maxValue: Int, isMax: Boolean) {
        valueText.setText(valueToString(currentValue, maxValue, isMax))

        seekBar.progress = convertValueToProgress(currentValue, maxValue)
        seekBar.setOnSeekBarChangeListener( object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val value = convertProgressToValue(progress, maxValue)
                    valueText.setText(valueToString(value, maxValue, isMax))
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun convertProgressToValue(progress : Int, maxValue : Int) : Int {
        return (progress.toFloat()/100 * maxValue).toInt()
    }

    private fun convertValueToProgress(value : Int, maxValue : Int) : Int {
        return (value.toFloat()/maxValue * 100).toInt()
    }

    private fun valueToString(currentValue: Int, maxValue: Int, isMax : Boolean) : String {
        return if (isMax && currentValue == maxValue) "None" else currentValue.toString()
    }
}