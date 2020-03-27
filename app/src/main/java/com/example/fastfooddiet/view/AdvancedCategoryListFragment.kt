package com.example.fastfooddiet.view

import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.fastfooddiet.R
import com.example.fastfooddiet.viewmodels.SharedViewModel

/*
 * Used to select categories when running an advanced search
 */
class AdvancedCategoryListFragment : CategoryListFragment() {

    private val sharedViewModel : SharedViewModel by navGraphViewModels(R.id.nav_advanced_search)

    override fun navigateToNext() {
        val selectedItems = categoryListViewModel.getSelectedItems()

        when (args.CategoryType) {
            CategoryType.RESTAURANT -> sharedViewModel.selectedRestaurant = selectedItems
            CategoryType.FOOD_TYPE -> sharedViewModel.selectedFoodTypes = selectedItems
        }

        categoryListViewModel.clearSelectedItems()
        closeKeyboard()
        findNavController().popBackStack()
    }
}