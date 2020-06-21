package com.example.fastfooddiet.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.fastfooddiet.data.*
import com.example.fastfooddiet.view.FoodListMode
import kotlinx.coroutines.launch

class FoodListViewModel (application: Application) : AndroidViewModel(application) {

    private val foodRepo : FoodRepo

    init {
        val foodDao = AppDatabase.getDatabase(application).foodDao()
        foodRepo = FoodRepo(foodDao)
    }

    //**** DIRECT SEARCH ******
    private val directQuery = MutableLiveData<String>()
    var directSearchQuery = ""

    fun directSearch(query : String) {
        Log.d("xfast", "Direct Query: " + query)
        directSearchQuery = query
        directQuery.value = if (query.isBlank()) "" else "%$query%"
    }

    //**** BROWSE SEARCH *****
    private val browseQuery = MutableLiveData<Triple<String, String, String>>()
    var browseSearchQuery = ""

    fun browseSearch(query:String, browseParams : BrowseParams) {
        Log.d("xfast", "Browse Query: " + query)
        browseSearchQuery = query
        browseQuery.value = Triple("%$query%", browseParams.restaurant, browseParams.foodType)
    }

    //**** CUSTOM SEARCH ******
    private val filteredQuery = MutableLiveData<SupportSQLiteQuery>()
    var filteredSearchQuery = ""

    fun filteredSearch(query:String, searchParams: SearchParams) {
        if (!query.isBlank()) {
            val updatedSearchParams = searchParams.copy(query = query)
            filteredQuery.value = foodRepo.rawQueryBuilder(updatedSearchParams)
        } else
            filteredQuery.value = foodRepo.rawQueryBuilder(searchParams)

        filteredSearchQuery = query
    }

    //**** EMPTY RESULTS RELATED ******
    val isEmptyTextVisible = MutableLiveData<Boolean>(false)
    val isHeaderVisible = MutableLiveData<Boolean>(false)

    fun isEmptyTextVisible(isVisible : Boolean, mode: FoodListMode) {

        if (mode == FoodListMode.DIRECT && directSearchQuery.isBlank()) {
            isEmptyTextVisible.value = false
            isHeaderVisible.value = false
            return
        }

        isHeaderVisible.value = true
        isEmptyTextVisible.value = isVisible
    }

    //**** OTHER ******

    fun getFoodResults(mode : FoodListMode): LiveData<List<Food>> {
        return when (mode) {
            FoodListMode.DIRECT -> directQuery.switchMap { foodRepo.searchFoodsOneSize(it) }
            FoodListMode.BROWSE -> browseQuery.switchMap { foodRepo.browseFoods(it.first, it.second, it.third) }
            FoodListMode.CUSTOM -> filteredQuery.switchMap { foodRepo.filteredSearch(it) }
        }
    }

    fun setFavorite(id: Int, isFavorite : Boolean) {
        viewModelScope.launch {
            foodRepo.setFavorite(id, !isFavorite)
        }
    }

    var showKeyboardOnStart : Boolean? = null

}