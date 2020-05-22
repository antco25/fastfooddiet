package com.example.fastfooddiet.data

import android.content.Context

//TODO: Remove from JSON restaurantIcon and foodTypeIcon and replace with this
class FoodIconConvert () {
    companion object {
        @Volatile
        private var INSTANCE : FoodIconConvert? = null

        fun getInstance(context: Context) : FoodIconConvert? {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildInstance(context).also { INSTANCE = it }
            }
        }

        private fun buildInstance(context: Context) : FoodIconConvert? {
            return null
        }
    }

}