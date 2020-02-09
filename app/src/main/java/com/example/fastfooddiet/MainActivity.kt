package com.example.fastfooddiet

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    //**** FIELDS ****

    //**** LIFECYCLE METHODS ****
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Load MainFragment
        //val mainFragment = MainFragment()
        val mainFragment = SearchListFragment()
        val fragmentManager = supportFragmentManager

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                .add(R.id.main_container, mainFragment)
                .commit()
        }

        //Mainly for SearchView queries
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        handleIntent(intent)
    }

    //**** METHODS ****
    private fun handleIntent(intent : Intent?) {

        if (Intent.ACTION_SEARCH == intent?.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            Log.d("Logger", query)
        }

    }
}
