package com.example.fastfooddiet.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "nutrition_table")
data class Nutrition (
    @PrimaryKey
    val id : Int,
    var calories : Int,
    var fat : Int,
    var satfat : Int,
    var transfat : Int,
    var chol : Int,
    var sodium : Int,
    var carbs : Int,
    var sugar : Int,
    var fiber : Int,
    var protein : Int
) {
    fun updateData(key : String, value : Int) {
        when (key) {
            "cal" -> calories = value
            "fat" -> fat = value
            "sfat" -> satfat = value
            "tfat" -> transfat = value
            "chol" -> chol = value
            "sod" -> sodium = value
            "carb" -> carbs = value
            "sug" -> sugar = value
            "fib" -> fiber = value
            "pro" -> protein = value
        }
    }
}


