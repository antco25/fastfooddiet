package com.example.fastfooddiet.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.fastfooddiet.data.*
import java.lang.IllegalArgumentException

class CustomSearchViewModel (application: Application) : AndroidViewModel(application) {

    /*
    SET RESTAURANT & FOOD TYPE FILTER ITEMS
    */

    val restaurantItems : LiveData<Array<String>>
    val foodTypeItems : LiveData<Array<String>>

    init {
        AppDatabase.getDatabase(application).apply {
            restaurantItems = this.foodDao().getAllRestaurants()
            foodTypeItems = this.foodDao().getAllFoodTypes()
        }
    }

    val restaurants = MutableLiveData<CategoryFilter>()
    private var _restaurants = CategoryFilter(emptyArray())

    fun getRestaurants() : CategoryFilter {
        return _restaurants
    }

    fun updateRestaurants(updated : CategoryFilter) {
        _restaurants = updated
        restaurants.value = _restaurants
    }

    fun updateCheckedRestaurants(_isCheckedItems : BooleanArray) {
        _restaurants.updateCheckedItems(_isCheckedItems)
        restaurants.value = _restaurants
    }

    val foodTypes = MutableLiveData<CategoryFilter>()
    private var _foodTypes = CategoryFilter(emptyArray())

    fun getFoodTypes() : CategoryFilter {
        return _foodTypes
    }

    fun updateFoodTypes(updated : CategoryFilter) {
        _foodTypes = updated
        foodTypes.value = _foodTypes
    }

    fun updateCheckedFoodTypes(_isCheckedItems : BooleanArray) {
        _foodTypes.updateCheckedItems(_isCheckedItems)
        foodTypes.value = _foodTypes
    }

    /*
    NUTRITION DATA FILTERS
     */

    private val maxNutritionData = mapOf(
        "calorieMax" to 1000
    )

    fun getMaxNutritionData(key : String) : Int {
        maxNutritionData[key]?.let { return it }
        throw IllegalArgumentException("Key not found")
    }

    val nutritionData = MutableLiveData<Map<String,Int>>()

    private val _nutritionData = mutableMapOf(
        "calorieMin" to 300,
        "calorieMax" to 800
    )

    fun getNutritionData(key: String) : Int {
        _nutritionData[key]?.let { return it }
        throw IllegalArgumentException("Key not found")
    }

    init {
        nutritionData.value = _nutritionData
    }

    fun updateNutritionData(maxKey: String, minKey : String, isMax : Boolean, value: Int) {

        /*
        If (Max Value) is set to "0" reset values
        If (Max Value) is lower or equals (Min Value), set (Min Value) to "0"

        If (Min Value) is set to "No Limit" reset values
        If (Min Value) exceeds or equals (Max Value), set (Max Value) to "No Limit"

        Note: No Limit is -1
         */

        val currentMax = _nutritionData[maxKey]!!
        val currentMin = _nutritionData[minKey]!!
        val maxLimit = maxNutritionData[maxKey]!!

        if (isMax) {
            if (value == 0) {
                _nutritionData[minKey] = 0
                _nutritionData[maxKey] = -1
            } else if (value <= currentMin) {
                _nutritionData[minKey] = 0
                _nutritionData[maxKey] = value
            } else if (value == maxLimit) {
                _nutritionData[maxKey] = -1
            } else {
                _nutritionData[maxKey] = value
            }
        } else {
            if (value == maxLimit) {
                _nutritionData[minKey] = 0
                _nutritionData[maxKey] = -1
            } else if (value >= currentMax) {
                _nutritionData[minKey] = value
                _nutritionData[maxKey] = -1
            } else {
                _nutritionData[minKey] = value
            }
        }

        nutritionData.value = _nutritionData
    }

    /*
    FILTER EXPANDED STATE
     */

    val isFilterVisible = MutableLiveData<BooleanArray>()
    private val _isFilterVisible = booleanArrayOf(
        true, //[0] - Restaurant Filter
        true, //[1] - Food Type Filter
        true) //[2] - Calories Type Filter

    init {
        isFilterVisible.value = _isFilterVisible
    }

    fun changeFilterVisibility(index : Int) {
        _isFilterVisible[index] = !_isFilterVisible[index]
        isFilterVisible.value = _isFilterVisible
    }


}