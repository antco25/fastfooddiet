package com.example.fastfooddiet.data

/*
  Used as part of CategoryFragment
 */

//TODO: Change this to just use string?

abstract class Category {
    abstract fun name() : String
}

data class Restaurant (
    val restaurant : String
) : Category() {
    override fun name(): String { return restaurant }
}

data class FoodType (
    val foodType : String
) : Category() {
    override fun name(): String { return foodType }
}

