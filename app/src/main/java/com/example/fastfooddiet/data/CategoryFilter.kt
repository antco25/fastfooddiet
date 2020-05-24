package com.example.fastfooddiet.data

/*
  Used as part of CustomSearchFragment
 */

class CategoryFilter (_items : Array<String>) {

    val items : Array<String> = arrayOf("All") + _items
    var isCheckedItems : BooleanArray
        private set
    var checkedItems : List<String>
        private set

    init {
        isCheckedItems = BooleanArray(items.size) {true}
        checkedItems = _items.toList()
    }

    fun updateCheckedItems(_isCheckedItems : BooleanArray) {

        if (_isCheckedItems.size != isCheckedItems.size)
            throw IllegalArgumentException("Checked item sizes not matching")

        isCheckedItems = _isCheckedItems

        //Case: All items selected
        if (_isCheckedItems[0]) {
            val allItems = items.toMutableList()
            allItems.removeAt(0) //Do not include "All"
            checkedItems = allItems
            return
        }

        //General case
        val _checkedItems = mutableListOf<String>()
        _isCheckedItems.mapIndexed {index, isChecked ->
            if (isChecked)
                _checkedItems.add(items[index])
        }
        checkedItems = _checkedItems
    }

    fun getCheckedItemsForView() : List<String> {
        if (isCheckedItems[0])
            return listOf("All")

        return checkedItems
    }

    fun getCheckedItemsForSearch() : List<String>? {
        if (isCheckedItems[0])
            return null

        return checkedItems
    }

    fun isEmpty() : Boolean {
        return (items.size == 1)
    }
}
