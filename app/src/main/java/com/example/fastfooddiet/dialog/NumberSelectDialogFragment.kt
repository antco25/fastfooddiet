package com.example.fastfooddiet.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.fastfooddiet.R
import com.example.fastfooddiet.viewmodels.CustomSearchViewModel
import kotlin.math.roundToInt

class NumberSelectDialogFragment : DialogFragment() {

    //**** PROPERTIES ****
    private val customSearchViewModel : CustomSearchViewModel by navGraphViewModels(R.id.nav_custom_search)
    private val args: NumberSelectDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val defaultMaxValue = customSearchViewModel
                .maxNutritionData[args.maxKey] ?: error("Default Max Key not found")

            val currentValue = if (args.isMax) {
                val max = customSearchViewModel.getNutritionData(args.maxKey)
                if (max == -1f) defaultMaxValue else max
            } else {
                customSearchViewModel.getNutritionData(args.minKey)
            }

            val title = if (args.isMax) "Set maximum value" else "Set minimum value"
            val view = it.layoutInflater.inflate(R.layout.dialog_number_select, null)
                .apply {  findViewById<TextView>(R.id.num_select_dialog_title).text = title }
            val valueText = view.findViewById<TextView>(R.id.num_select_dialog_value)
            val seekBar = view.findViewById<SeekBar>(R.id.num_select_dialog_seekBar)

            setViews(valueText, seekBar, currentValue, defaultMaxValue, args.isMax, args.isFloat)

            val builder = AlertDialog.Builder(it)
            builder.setView(view)
                .setPositiveButton("Ok") { _, _ ->
                        customSearchViewModel.updateNutritionData(args.maxKey, args.minKey,
                            args.isMax, convertProgressToValue(seekBar.progress, defaultMaxValue))
                    }
                .setNegativeButton("Cancel") { _, _ -> }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    //**** METHODS ****
    private fun setViews(valueText: TextView,
                         seekBar: SeekBar,
                         currentValue : Float,
                         maxValue: Float,
                         isMax: Boolean,
                         isFloat: Boolean) {

        valueText.text = valueToString(currentValue, maxValue, isMax, isFloat)

        seekBar.progress = convertValueToProgress(currentValue, maxValue)
        seekBar.setOnSeekBarChangeListener( object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val value = convertProgressToValue(progress, maxValue)
                    valueText.text = valueToString(value, maxValue, isMax, isFloat)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun convertProgressToValue(progress : Int, maxValue : Float) : Float {
        val value = progress.toFloat()/100 * maxValue

        //Round value to 1 decimal
        return (value * 10).roundToInt() / 10f
    }

    private fun convertValueToProgress(value : Float, maxValue : Float) : Int {
        return (value/maxValue * 100).toInt()
    }

    private fun valueToString(currentValue: Float,
                              maxValue: Float,
                              isMax : Boolean,
                              isFloat: Boolean) : String {
        return when {
            isMax && currentValue == maxValue -> "None"
            isFloat -> currentValue.toString()
            else -> currentValue.toInt().toString()
        }
    }
}