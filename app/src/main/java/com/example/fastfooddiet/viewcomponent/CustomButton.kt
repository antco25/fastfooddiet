package com.example.fastfooddiet.viewcomponent

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.marginEnd
import com.example.fastfooddiet.R

class CustomButton : TextView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    private val selectedTextColor = Color.WHITE
    private val defaultTextColor = ContextCompat.getColor(context,R.color.secondary_light)

    fun onSelectStatus() {
        if (isSelected) {
            isSelected = false
            setTextColor(defaultTextColor)
        } else {
            isSelected = true
            setTextColor(selectedTextColor)

        }
    }

    fun setSelectStatus(isSelected : Boolean) {
        this.isSelected = isSelected

        if (isSelected) {
            setTextColor(selectedTextColor)
        } else {
            setTextColor(defaultTextColor)
        }
    }
}