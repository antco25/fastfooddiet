package com.example.fastfooddiet.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.fastfooddiet.data.*
import com.example.fastfooddiet.view.CategoryListFragment.CategoryType
import kotlinx.coroutines.launch

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
    FILTER EXPANDED STATE
     */

    val isFilterVisible = MutableLiveData<BooleanArray>()
    private val _isFilterVisible = booleanArrayOf(
        true, //[0] - Restaurant Filter
        true) //[1] - Food Type Filter

    init {
        isFilterVisible.value = _isFilterVisible
    }

    fun changeFilterVisibility(index : Int) {
        _isFilterVisible[index] = !_isFilterVisible[index]
        isFilterVisible.value = _isFilterVisible
    }

}