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

    //TODO: Fix slider issues, sometimes you move slider but it doesn't change number
    val maxNutritionData = mapOf(
        "calorieMax" to 1000f,
        "fatMax" to 50f,
        "sfatMax" to 20f,
        "tfatMax" to 5f,
        "cholMax" to 150f,
        "sodiumMax" to 1000f,
        "carbMax" to 50f,
        "sugarMax" to 10f,
        "fiberMax" to 5f,
        "proteinMax" to 30f
    )

    val nutritionData = MutableLiveData<Map<String,Float>>()

    private val _nutritionData = mutableMapOf(
        "calorieMin" to 0f,
        "calorieMax" to -1f,
        "fatMin" to 0f,
        "fatMax" to -1f,
        "sfatMin" to 0f,
        "sfatMax" to -1f,
        "tfatMin" to 0f,
        "tfatMax" to -1f,
        "cholMin" to 0f,
        "cholMax" to -1f,
        "sodiumMin" to 0f,
        "sodiumMax" to -1f,
        "carbMin" to 0f,
        "carbMax" to -1f,
        "sugarMin" to 0f,
        "sugarMax" to -1f,
        "fiberMin" to 0f,
        "fiberMax" to -1f,
        "proteinMin" to 0f,
        "proteinMax" to -1f
    )

    fun getNutritionData(key: String) : Float {
        return _nutritionData[key] ?: error("Key not found")
    }

    init {
        nutritionData.value = _nutritionData
    }

    fun updateNutritionData(maxKey: String,
                            minKey : String,
                            isMax : Boolean,
                            value: Float) {

        /*
        If (Max Value) is set to "0" reset values
        If (Max Value) is lower or equals (Min Value), set (Min Value) to "0"

        If (Min Value) exceeds or equals (Max Value), set (Max Value) to "No Limit"

        Note: No Limit is -1
         */

        val currentMax = _nutritionData[maxKey] ?: error("Max Key not found")
        val currentMin = _nutritionData[minKey] ?: error("Min Key not found")
        val maxLimit = maxNutritionData[maxKey] ?: error("Default Max Key not found")

        if (isMax) {
            if (value == 0f) {
                _nutritionData[minKey] = 0f
                _nutritionData[maxKey] = -1f
            } else if (value <= currentMin) {
                _nutritionData[minKey] = 0f
                _nutritionData[maxKey] = value
            } else if (value == maxLimit) {
                _nutritionData[maxKey] = -1f
            } else {
                _nutritionData[maxKey] = value
            }
        } else {
            if (value >= currentMax) {
                _nutritionData[minKey] = value
                _nutritionData[maxKey] = -1f
            } else {
                _nutritionData[minKey] = value
            }
        }

        nutritionData.value = _nutritionData
    }

    fun getSearchParams() : SearchParams {
        return SearchParams(
            query = "",
            restaurants = _restaurants.getCheckedItemsForSearch(),
            foodType = _foodTypes.getCheckedItemsForSearch(),
            favorites = 0,
            caloriesMin = _nutritionData["calorieMin"]!!.toInt(),
            caloriesMax = _nutritionData["calorieMax"]!!.toInt(),
            proteinMin = _nutritionData["proteinMin"]!!.toInt(),
            proteinMax = _nutritionData["proteinMax"]!!.toInt(),
            fatMin = _nutritionData["fatMin"]!!.toInt(),
            fatMax = _nutritionData["fatMax"]!!.toInt(),
            sfatMin = _nutritionData["sfatMin"]!!.toInt(),
            sfatMax = _nutritionData["sfatMax"]!!.toInt(),
            tfatMin = _nutritionData["tfatMin"]!!,
            tfatMax = _nutritionData["tfatMax"]!!,
            cholMin = _nutritionData["cholMin"]!!.toInt(),
            cholMax = _nutritionData["cholMax"]!!.toInt(),
            sodiumMin = _nutritionData["sodiumMin"]!!.toInt(),
            sodiumMax = _nutritionData["sodiumMax"]!!.toInt(),
            carbMin = _nutritionData["carbMin"]!!.toInt(),
            carbMax = _nutritionData["carbMax"]!!.toInt(),
            sugarMin = _nutritionData["sugarMin"]!!.toInt(),
            sugarMax = _nutritionData["sugarMax"]!!.toInt(),
            fiberMin = _nutritionData["fiberMin"]!!.toInt(),
            fiberMax = _nutritionData["fiberMax"]!!.toInt()
        )
    }

    /*
    FILTER EXPANDED STATE
     */

    val isFilterVisible = MutableLiveData<BooleanArray>()
    private val _isFilterVisible = booleanArrayOf(
        true, //[0] - Restaurant Filter
        true, //[1] - Food Type Filter
        true, //[2] - Calories Filter
        false, //[3] - Protein Filter
        false, //[4] - Fat Filter
        false, //[5] - Saturated Fat Filter
        false, //[6] - Trans Fat Filter
        false, //[7] - Cholesterol Filter
        false, //[8] - Sodium Filter
        false, //[9] - Carbohydrates Filter
        false, //[10] - Sugar Filter
        false  //[11] - Fiber Filter
    )

    init {
        isFilterVisible.value = _isFilterVisible
    }

    fun changeFilterVisibility(index : Int) {
        _isFilterVisible[index] = !_isFilterVisible[index]
        isFilterVisible.value = _isFilterVisible
    }



}