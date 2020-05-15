package com.example.fastfooddiet.data

/*
  Used as part of CategoryFragment
 */

abstract class Category {
    abstract fun name() : String
    abstract fun icon() : String
}

data class Restaurant (
    val restaurant : String,
    val restaurantIcon : String
) : Category() {
    override fun name(): String { return restaurant }
    override fun icon(): String { return restaurantIcon }
}

data class FoodType (
    val foodType : String,
    val foodTypeIcon: String
) : Category() {
    override fun name(): String { return foodType }
    override fun icon(): String { return foodTypeIcon }
}

