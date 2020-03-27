package com.example.fastfooddiet.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.fastfooddiet.data.AppDatabase
import com.example.fastfooddiet.data.Category
import com.example.fastfooddiet.data.FoodRepo
import com.example.fastfooddiet.view.CategoryListFragment.CategoryType

class CategoryListViewModel (application: Application) : AndroidViewModel(application) {

    private val foodRepo : FoodRepo

    init {
        AppDatabase.getDatabase(application).apply {
            foodRepo = FoodRepo(this.foodDao())
        }
    }

    private val searchQuery = MutableLiveData<String>()

    fun search(query : String?) {
        query?.let {
            if (!it.isBlank())
                searchQuery.value = "%$query%"
            else
                searchQuery.value = "%%"
        }
    }

    fun getSearchQuery() : String {
        searchQuery.value?.let {string ->
            return string.substring(1, string.length-1)
        }
        return ""
    }


    fun getCategoryResults(categoryType : CategoryType): LiveData<List<Category>> {
        return when (categoryType) {
            CategoryType.RESTAURANT -> searchQuery.switchMap { foodRepo.searchRestaurants(it) as LiveData<List<Category>> }
            CategoryType.FOOD_TYPE -> searchQuery.switchMap { foodRepo.searchFoodType(it) as LiveData<List<Category>> }
        }
    }

    val isMultipleSelect = MutableLiveData<Boolean>(false)

    fun setMultipleSelect(isMultipleSelect : Boolean) {
        this.isMultipleSelect.value = isMultipleSelect
    }

    fun checkMultipleSelect() : Boolean {
        isMultipleSelect.value?.let { return it }
        return false
    }

    val selectedItems = MutableLiveData<Set<String>>()
    private val _selectedItems = mutableSetOf<String>()

    fun onItemClick(item : String) {
        if (_selectedItems.contains(item))
            _selectedItems.remove(item)
        else
            _selectedItems.add(item)

        selectedItems.value = _selectedItems
        Log.d("Logger", "${item} selected, total selected: ${_selectedItems.size}")
    }

    fun getSelectedItems() : List<String>? {
        if (_selectedItems.size > 0)
            return _selectedItems.distinct()
        return null
    }

    fun clearSelectedItems() {
        _selectedItems.clear()
        selectedItems.value = _selectedItems
    }


}