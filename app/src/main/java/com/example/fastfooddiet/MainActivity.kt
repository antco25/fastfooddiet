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
    }
}
