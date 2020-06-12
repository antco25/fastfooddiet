package com.example.fastfooddiet.data

interface ListItem {
    val itemName : String
    val itemId : Int
    fun description(showItemDetailWithSize : Boolean) : String
}