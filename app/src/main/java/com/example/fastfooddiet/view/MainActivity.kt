package com.example.fastfooddiet.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fastfooddiet.R

class MainActivity : AppCompatActivity() {

    //**** LIFECYCLE METHODS ****
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
