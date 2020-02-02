package com.example.fastfooddiet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class MainFragment : Fragment() {

    //**** FIELDS ****
    private var buttonClicks = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false);

        if (savedInstanceState != null)
            buttonClicks = savedInstanceState.getInt("buttonClicks", 0)

        val textView = view.findViewById<TextView>(R.id.textView)
        textView.setText("Button clicked: " + buttonClicks + " times")
        val button = view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            buttonClicks++
            textView.setText("Button clicked: " + buttonClicks + " times")
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("buttonClicks", buttonClicks)
    }
}