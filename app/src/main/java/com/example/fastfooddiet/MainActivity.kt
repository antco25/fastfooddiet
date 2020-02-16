package com.example.fastfooddiet

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.fastfooddiet.data.AppDatabase
import com.example.fastfooddiet.viewmodels.FoodViewModel
import com.example.fastfooddiet.worker.SeedDatabaseWorker

class MainActivity : AppCompatActivity() {

    //**** FIELDS ****
    private lateinit var foodViewModel : FoodViewModel

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

        //TODO: Delete this
        val db = AppDatabase.getDatabase(this)

        //Mainly for SearchView queries
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
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
