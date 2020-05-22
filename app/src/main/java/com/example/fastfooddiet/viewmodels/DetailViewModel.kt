package com.example.fastfooddiet.viewmodels

import android.app.Application
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.*
import com.example.fastfooddiet.data.*
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val foodRepo : FoodRepo
    private val nutritionRepo : NutritionRepo

    init {
        val foodDao = AppDatabase.getDatabase(application).foodDao()
        foodRepo = FoodRepo(foodDao)

        val nutritionDao = AppDatabase.getDatabase(application).nutritionDao()
        nutritionRepo = NutritionRepo(nutritionDao)
    }

    private val foodId = MutableLiveData<Int>()

    fun setFood(id : Int) {
        foodId.value = id
    }

    val food : LiveData<Food> = foodId.switchMap {
        foodRepo.getFood(it)
    }

    val isCustomNutritionData = MutableLiveData<Boolean>(false)

    val nutrition : LiveData<Nutrition> = isCustomNutritionData.switchMap { isCustomData ->
        if (isCustomData)
            nutritionRepo.getCustomNutrition()
        else
            nutritionRepo.getMasterNutrition()
    }

    fun setFavorite(id: Int, isFavorite : Boolean) {
        viewModelScope.launch {
            foodRepo.setFavorite(id, !isFavorite)
        }
    }
}