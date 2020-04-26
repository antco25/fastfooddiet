package com.example.fastfooddiet.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.fastfooddiet.data.*
import com.example.fastfooddiet.view.CategoryListFragment.CategoryType
import kotlinx.coroutines.launch

class CustomSearchViewModel (application: Application) : AndroidViewModel(application) {

    private val foodRepo : FoodRepo
    val restaurants : LiveData<Array<String>>
    val foodTypes : LiveData<Array<String>>

    init {
        AppDatabase.getDatabase(application).apply {
            foodRepo = FoodRepo(this.foodDao())
            foodTypes = Transformations.map(this.foodDao().getAllFoodTypes()) {
                arrayOf("All") + it
            }
            restaurants = Transformations.map(this.foodDao().getAllRestaurants()) {
                arrayOf("All") + it
            }
        }
    }

    val checkedRestaurants = MutableLiveData<BooleanArray>()
    val checkedFoodTypes = MutableLiveData<BooleanArray>()
    private var isNewFoodTypes = false
    private var isNewRestaurants = false

    fun getCheckedRestaurants(newSize : Int) : BooleanArray {
        checkedRestaurants.value?.let {
            if (!isNewRestaurants)
                return it
        }

        //Reset checked items if new or null data
        val _checkedRestaurants = BooleanArray(newSize) {true}
        checkedRestaurants.value = _checkedRestaurants
        return _checkedRestaurants
    }

    fun getCheckedFoodTypes(newSize : Int) : BooleanArray {
        checkedFoodTypes.value?.let {
            if (!isNewFoodTypes)
                return it
        }

        //Reset checked items if new or null data
        val _checkedFoodTypes = BooleanArray(newSize) {true}
        checkedFoodTypes.value = _checkedFoodTypes
        return _checkedFoodTypes
    }

}