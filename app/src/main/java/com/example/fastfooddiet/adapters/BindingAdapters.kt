package com.example.fastfooddiet.adapters

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.fastfooddiet.R
import com.example.fastfooddiet.viewcomponent.BarGraph
import com.example.fastfooddiet.viewcomponent.CustomButton

@BindingAdapter("topDrawable")
fun setTopDrawable(textView: TextView, resource : String) {

    val resourceId = textView.context.run {
        resources.getIdentifier(resource, "drawable", packageName)
    }

    textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0,resourceId,0,0)
}

@BindingAdapter("iconResource")
fun setIconImage(imageView: ImageView, resource : String) {

    /* getIdentifier is too slow?
    val resourceId = imageView.context.run {
        resources.getIdentifier(resource, "drawable", packageName)
    }

    imageView.setImageResource(resourceId)
     */

    stringToResId(resource)?.let {
        imageView.setImageResource(it)
    }

}

private fun stringToResId(string : String) : Int? {
    return when (string) {
        "Baked Goods" -> R.drawable.ft_wrap
        "Breakfast" -> R.drawable.ft_wrap
        "Burgers" -> R.drawable.ft_burger
        "Chicken" -> R.drawable.ft_chicken
        "Coffee & Hot Drinks" -> R.drawable.ft_wrap
        "Condiments" -> R.drawable.ft_wrap
        "Dessert" -> R.drawable.ft_wrap
        "Sandwiches" -> R.drawable.ft_sandwich
        "Salads" -> R.drawable.ft_wrap
        "Sides & Snacks" -> R.drawable.ft_wrap
        "Burger King" -> R.drawable.rest_bk
        "KFC" -> R.drawable.rest_kfc
        "Mcdonald's" -> R.drawable.rest_mcd
        else -> null
    }
}

@BindingAdapter("barValue", "barLimit")
fun setBarGraph(barGraph: BarGraph, value: Int, limit: Int) {
    Log.d("xfast" ,"Value: " + value + " Limit: " + limit)
    barGraph.setValues(value.toFloat(), limit.toFloat())
}

@BindingAdapter("barValue", "barLimit")
fun setBarGraph(barGraph: BarGraph, value: Float, limit: Float) {
    Log.d("xfast" ,"Value: " + value + " Limit: " + limit)
    barGraph.setValues(value, limit)
}


@BindingAdapter("buttonSelected")
fun setSelectedButton(button : CustomButton, isSelected: Boolean) {
    button.setSelectStatus(isSelected)
}

