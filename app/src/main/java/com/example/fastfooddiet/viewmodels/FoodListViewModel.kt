package com.example.fastfooddiet.viewmodels

import android.app.Application
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.widget.TextView
import androidx.core.text.bold
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.fastfooddiet.data.AppDatabase
import com.example.fastfooddiet.data.Food
import com.example.fastfooddiet.data.FoodRepo
import com.example.fastfooddiet.data.SearchParams

class FoodListViewModel (application: Application) : AndroidViewModel(application) {

    private val foodRepo : FoodRepo

    init {
        val foodDao = AppDatabase.getDatabase(application).foodDao()
        foodRepo = FoodRepo(foodDao)
    }

    var showAllResultsDefault = false
    private val searchQuery = MutableLiveData<String>()

    fun search(query : String?) {
        query?.let {
            Log.d("Logger", "New Query: ${it}")
            if (!it.isBlank())
                searchQuery.value = "%$query%"
            else if (showAllResultsDefault)
                searchQuery.value = "%%"
            else
                searchQuery.value = ""
        }
    }

    fun getSearchQuery() : String {
        searchQuery.value?.let {string ->
            if (string.isBlank()) return ""
            return string.substring(1, string.length-1)
        }
        return ""
    }

    fun getFoodResults(searchParams: SearchParams?): LiveData<List<Food>> {
        searchParams?.let {
            return getFilteredFoodResults(it)
        }

        return searchQuery.switchMap { foodRepo.searchFoods(it) }
    }

    private val _filteredSearchQuery = MutableLiveData<SupportSQLiteQuery>()
    var filteredSearchQuery = ""

    fun filteredSearch(query:String?, searchParams: SearchParams) {
        query?.let {
            if (!it.isBlank()) {
                val updatedSearchParams = searchParams.copy(query = it)
                _filteredSearchQuery.value = foodRepo.rawQueryBuilder(updatedSearchParams)
            } else
                _filteredSearchQuery.value = foodRepo.rawQueryBuilder(searchParams)

            filteredSearchQuery = it
        }
    }

    //TODO: Search params never used
    private fun getFilteredFoodResults(searchParams: SearchParams) : LiveData<List<Food>> {
        return _filteredSearchQuery.switchMap { foodRepo.filteredSearch(it) }
    }

    val isEmptyTextVisible = MutableLiveData<Boolean>(false)

    fun isEmptyTextVisible(isVisible : Boolean) {
        val isSearchQueryEmpty = searchQuery.value?.let { it.isBlank() } ?: true

        if (!showAllResultsDefault && isSearchQueryEmpty)
            isEmptyTextVisible.value = false
        else
            isEmptyTextVisible.value = isVisible
    }

    fun getEmptyResultsText() : SpannableString {
        val headerText = "No results found\n"
        val normalText = "   Try another category or keyword\n" +
                "   Make sure your keyword is spelled correctly"

        return SpannableString(headerText + normalText).apply {
            setSpan(AbsoluteSizeSpan(12,true),headerText.length,
                headerText.length+normalText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    override fun onCleared() {
        Log.d("Logger", "FoodListViewModel cleared")
        super.onCleared()
    }

}