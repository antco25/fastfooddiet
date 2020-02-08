package com.example.fastfooddiet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    //**** FIELDS ****

    //**** LIFECYCLE METHODS ****
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Load MainFragment
        val mainFragment = SearchListFragment()
        val fragmentManager = supportFragmentManager

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                .add(R.id.main_container, mainFragment)
                .commit()
        }

    }
}
