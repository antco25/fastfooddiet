package com.example.fastfooddiet.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("topDrawable")
fun setTopDrawable(textView: TextView, resource : String) {

    val resourceId = textView.context.run {
        resources.getIdentifier(resource, "drawable", packageName)
    }

    textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0,resourceId,0,0)
}

@BindingAdapter("imageResource")
fun setImageResource(imageView: ImageView, resource : String) {

    val resourceId = imageView.context.run {
        resources.getIdentifier(resource, "drawable", packageName)
    }

    imageView.setImageResource(resourceId)
}