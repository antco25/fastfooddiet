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
        var servingSize = 0
        var calories = 0
        var fat = 0
        var satfat = 0
        var transfat = 0
        var chol = 0
        var sodium = 0
        var carbs = 0
        var sugar = 0
        var fiber = 0
        var protein = 0

        meal.foods.map { food ->
            servingSize += food.servingSize
            calories += food.calories
            fat += food.fat
            satfat += food.satfat
            transfat += food.transfat
            chol += food.chol
            sodium += food.sodium
            carbs += food.carbs
            sugar += food.sugar
            fiber += food.fiber
            protein += food.protein
        }

        Food(foodId = 0,
            name = "",
            restaurant = "",
            restaurantIcon = "",
            foodType = "",
            foodTypeIcon = "",
            favorite = false,
            servingSize = servingSize,
            calories = calories,
            fat = fat,
            satfat = satfat,
            transfat = transfat,
            chol = chol,
            sodium = sodium,
            carbs = carbs,
            sugar = sugar,
            fiber = fiber,
            protein = protein
        )
    }

}