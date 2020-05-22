package com.example.fastfooddiet.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.fastfooddiet.data.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val nutritionRepo : NutritionRepo

    init {
        val nutritionDao = AppDatabase.getDatabase(application).nutritionDao()
        nutritionRepo = NutritionRepo(nutritionDao)
    }

    val isCustomData = MutableLiveData<Boolean>(false)
    val nutritionData : LiveData<Nutrition> = isCustomData.switchMap { isCustomData ->
        if (isCustomData)
            nutritionRepo.getCustomNutrition()
        else
            nutritionRepo.getMasterNutrition()
    }

    fun updateCustomData(nutrition: Nutrition) = viewModelScope.launch {
        nutritionRepo.updateCustomNutrition(nutrition)
    }

    fun resetCustomData() = viewModelScope.launch {
        val master = nutritionRepo.getMasterNutritionRaw().apply {
            val copy = Nutrition(id = 1,
                calories = calories,
                fat = fat,
                satfat = satfat,
                transfat = transfat,
                chol = chol,
                sodium = sodium,
                carbs = carbs,
                sugar = sugar,
                fiber = fiber,
                protein = protein)

            nutritionRepo.updateCustomNutrition(copy)
        }
    }

}