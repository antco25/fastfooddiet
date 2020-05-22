package com.example.fastfooddiet.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_table")
data class Food (
    @PrimaryKey (autoGenerate = true)
    val id : Int,
    val name : String,
    val restaurant : String,
    val restaurantIcon : String,
    val foodType : String,
    val foodTypeIcon: String,
    val favorite : Boolean = false,
    val servingSize : Int,
    val calories : Int,
    val fat : Int,
    val satfat : Int,
    val transfat : Int,
    val chol : Int,
    val sodium : Int,
    val carbs : Int,
    val sugar : Int,
    val fiber : Int,
    val protein : Int
)
