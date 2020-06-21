package com.example.fastfooddiet.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fastfooddiet.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    //**** LIFECYCLE METHODS ****
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val bottomNav : BottomNavigationView = findViewById(R.id.bottom_nav)
        val navController = findNavController(R.id.nav_host)

        bottomNav.setupWithNavController(navController)

    }
}
