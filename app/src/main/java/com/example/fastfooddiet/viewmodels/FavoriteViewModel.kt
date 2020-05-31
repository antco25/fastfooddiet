package com.example.fastfooddiet.viewmodels

import android.app.Application
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.*
import com.example.fastfooddiet.data.*
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val foodRepo: FoodRepo
    private val nutritionRepo: NutritionRepo
    private val mealRepo: MealRepo

    init {
        AppDatabase.getDatabase(application).apply {
            foodRepo = FoodRepo(foodDao())
            nutritionRepo = NutritionRepo(nutritionDao())
            mealRepo = MealRepo(mealDao())
        }
    }

    //**** SWITCH BETWEEN FAVORITED FOOD AND MEALS****
    val isFavoriteFoods = MutableLiveData<Boolean>(true)

    fun setIsFoods(isFoods: Boolean) {
        isFavoriteFoods.value = isFoods
    }

    //**** OTHER ****

    val favoriteFoods = foodRepo.searchFavoriteFoods("%%")
    val meals = mealRepo.getMeals()

    fun addMeal(name : String) = viewModelScope.launch {
        mealRepo.insertMeal(MealData(name = name))
        Log.d("xfast", "Meal: $name inserted")
    }

    fun deleteMeal(mealId: Int) {
        viewModelScope.launch {
            mealRepo.deleteMealFoods(mealId)
            mealRepo.deleteMealData(mealId)
        }
    }

    fun setFavorite(id: Int, isFavorite : Boolean) {
        viewModelScope.launch {
            foodRepo.setFavorite(id, !isFavorite)
        }
    }

    val isDeleteMode = MutableLiveData<Boolean>(false)
    private var _isDeleteMode = false

    fun isDeleteMode() : Boolean {
        return _isDeleteMode
    }

    fun setDeleteMode(isDeleteMode : Boolean) {
        _isDeleteMode = isDeleteMode
        this.isDeleteMode.value = isDeleteMode
    }

}