package com.example.fastfooddiet.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.fastfooddiet.data.*
import kotlinx.coroutines.launch

class MealViewModel(application: Application) : AndroidViewModel(application) {

    private val mealRepo : MealRepo
    private val nutritionRepo : NutritionRepo

    init {
        AppDatabase.getDatabase(application).apply {
            mealRepo = MealRepo(mealDao())
            nutritionRepo = NutritionRepo(nutritionDao())
        }
    }

    val isCustomNutritionData = MutableLiveData<Boolean>(false)

    val nutrition : LiveData<Nutrition> = isCustomNutritionData.switchMap { isCustomData ->
        if (isCustomData)
            nutritionRepo.getCustomNutrition()
        else
            nutritionRepo.getMasterNutrition()
    }

    private val mealId = MutableLiveData<Int>()

    fun setMeal(id : Int) {
        mealId.value = id
    }

    val meal : LiveData<Meal> = mealId.switchMap {
        mealRepo.getMeal(it)
    }

    val isMealEmpty = MutableLiveData<Boolean>(false)

    val isFoodView = MutableLiveData<Boolean>(false)

    val isDeleteMode = MutableLiveData<Boolean>(false)
    private var _isDeleteMode = false

    fun isDeleteMode() : Boolean {
        return _isDeleteMode
    }

    fun setDeleteMode(isDeleteMode : Boolean) {
        _isDeleteMode = isDeleteMode
        this.isDeleteMode.value = isDeleteMode
    }

    var mealFoods : List<MealFoodCrossRef>? = null

    fun deleteMealFood(mealFoodId : Int) = viewModelScope.launch {
        mealRepo.deleteMealFood(mealFoodId)
    }

    fun updateMeal(mealData: MealData) = viewModelScope.launch {
        mealRepo.updateMealData(mealData)
    }

    val combinedFood : LiveData<Food> = Transformations.map(meal) {meal ->
        var calories = 0
        var fat = 0
        var satfat = 0
        var transfat = 0f
        var chol = 0
        var sodium = 0
        var carbs = 0
        var sugar = 0
        var fiber = 0
        var protein = 0

        meal.foods.map { food ->
            calories += foodValue(food.calories)
            fat += foodValue(food.fat)
            satfat += foodValue(food.satfat)
            transfat += if (food.transfat < 0f) 0f else food.transfat
            chol += foodValue(food.chol)
            sodium += foodValue(food.sodium)
            carbs += foodValue(food.carbs)
            sugar += foodValue(food.sugar)
            fiber += foodValue(food.fiber)
            protein += foodValue(food.protein)
        }

        Food(foodId = 0,
            name = "",
            restaurant = "",
            foodType = "",
            servingSize = "",
            calories = calories,
            fat = fat,
            satfat = satfat,
            transfat = transfat,
            chol = chol,
            sodium = sodium,
            carbs = carbs,
            sugar = sugar,
            fiber = fiber,
            protein = protein,
            sizeMode = 0,
            favorite = false
        )
    }

    fun foodValue(value : Int) : Int {
        return if (value < 0) 0 else value
    }

}