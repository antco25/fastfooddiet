package com.example.fastfooddiet.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.fastfooddiet.data.AppDatabase
import com.example.fastfooddiet.data.Food
import com.example.fastfooddiet.data.FoodRepo
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val foodRepo : FoodRepo

    init {
        val foodDao = AppDatabase.getDatabase(application).foodDao()
        foodRepo = FoodRepo(foodDao)
    }

    private val foodId = MutableLiveData<Int>()

    fun setFood(id : Int) {
        foodId.value = id
    }

    val food : LiveData<Food> = foodId.switchMap {
        foodRepo.getFood(it)
    }

    fun setFavorite(id: Int, isFavorite : Boolean) {
        viewModelScope.launch {
            foodRepo.setFavorite(id, isFavorite)
        }
    }
}