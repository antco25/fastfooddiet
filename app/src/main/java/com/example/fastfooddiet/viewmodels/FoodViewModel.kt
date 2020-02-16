package com.example.fastfooddiet.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fastfooddiet.data.AppDatabase
import com.example.fastfooddiet.data.Food
import com.example.fastfooddiet.data.FoodRepo
import kotlinx.coroutines.launch

class FoodViewModel(application: Application) : AndroidViewModel(application) {

    private val foodRepo : FoodRepo
    val foods : LiveData<List<Food>>

    init {
        val foodDao = AppDatabase.getDatabase(application).foodDao()
        foodRepo = FoodRepo(foodDao)
        foods = foodRepo.getFoods
    }

    fun deleteThis() = viewModelScope.launch {

        val foodList = listOf(
            Food(1, "BigMac","Burger",100),
            Food(2, "BigMac2","Burger",100),
            Food(3, "BigMac3","Burger",100),
            Food(4, "BigMac4","Burger",100),
            Food(5, "BigMac5","Burger",100),
            Food(6, "BigMac6","Burger",100),
            Food(7, "BigMac7","Burger",100),
            Food(8, "BigMac8","Burger",100),
            Food(9, "BigMac9","Burger",100),
            Food(10, "BigMac10","Burger",100)
        )
        foodRepo.insertFoods(foodList)
    }



}