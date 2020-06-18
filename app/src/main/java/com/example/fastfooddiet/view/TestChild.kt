package com.example.fastfooddiet.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.fastfooddiet.R

class TestChild : Fragment() {

    private var text = ""

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.test_child, container, false)

        val textView : TextView = view.findViewById(R.id.test_child_text)
        textView.text = text

        val imageView : ImageView = view.findViewById(R.id.test_child_image)
        val url = "https://miro.medium.com/max/1200/1*mk1-6aYaf_Bes1E3Imhc0A.jpeg"
        loadImage(imageView, url)

        return view
    }

    //**** METHODS ****

    fun setDefaultText(_text : String) {
        text = _text
    }

    private fun loadImage(imageView: ImageView, url : String) {
        Glide.with(this)
            .load(url)
            .apply(RequestOptions()
                .placeholder(R.drawable.ic_refresh)
                .error(R.drawable.ic_close))
            .into(imageView)
    }
}
