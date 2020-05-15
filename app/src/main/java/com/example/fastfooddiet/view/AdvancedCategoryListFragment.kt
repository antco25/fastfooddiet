package com.example.fastfooddiet.view

import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.fastfooddiet.R
import com.example.fastfooddiet.viewmodels.SharedViewModel

/*
 * Used to select categories when running an advanced search
 */

//TODO: Delete this
class AdvancedCategoryListFragment : CategoryFragment() {

    private val sharedViewModel : SharedViewModel by navGraphViewModels(R.id.nav_advanced_search)

    /*
    override fun navigateToNext() {

        val selectedItems = categoryViewModel.getSelectedItems()

        when (args.CategoryType) {
            CategoryType.RESTAURANT -> sharedViewModel.selectedRestaurant = selectedItems
            CategoryType.FOOD_TYPE -> sharedViewModel.selectedFoodTypes = selectedItems
        }

        categoryListViewModel.clearSelectedItems()
        closeKeyboard()
        findNavController().popBackStack()


    }
    */

}