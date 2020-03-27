package com.example.fastfooddiet.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.fastfooddiet.R
import com.example.fastfooddiet.data.SearchParams
import com.example.fastfooddiet.databinding.FragmentAdvancedSearchBinding
import com.example.fastfooddiet.viewmodels.SharedViewModel
import com.example.fastfooddiet.view.CategoryListFragment.CategoryType

class AdvancedSearchFragment : Fragment() {

    //**** PROPERTIES ****
    val sharedViewModel : SharedViewModel by navGraphViewModels(R.id.nav_advanced_search)

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAdvancedSearchBinding
            .inflate(inflater,container,false).apply {
                setupCategoryFilterText(filterRestaurant, filterFoodType)
                setupButtons(this)
            }
        return binding.root
    }

    //**** METHODS ****
    private fun setupButtons(binding : FragmentAdvancedSearchBinding) {
        binding.filterRestaurant.setOnClickListener {
            val action = AdvancedSearchFragmentDirections
                .toAdvancedCategoryList("Apply Restaurant Filter", CategoryType.RESTAURANT)
            findNavController().navigate(action)
        }

        binding.filterFoodType.setOnClickListener {
            val action = AdvancedSearchFragmentDirections
                .toAdvancedCategoryList("Apply Food Type Filter", CategoryType.FOOD_TYPE)
            findNavController().navigate(action)
        }

        binding.searchButton.setOnClickListener {
            val action = AdvancedSearchFragmentDirections
                .toFoodListFragment("Advanced Search Results",
                    false, true,
                    false, getSearchParams(binding))
            findNavController().navigate(action)
        }
    }

    private fun setupCategoryFilterText(restaurantText : TextView, foodTypeText : TextView) {
        sharedViewModel.selectedRestaurant?.let { list->
            restaurantText.setText(listToText(list))
        }

        sharedViewModel.selectedFoodTypes?.let { list->
            foodTypeText.setText(listToText(list))
        }
    }

    private fun listToText(list : List<String>) : String {
        var text = ""
        list.map { name -> text += "${name}, " }
        text = text.dropLast(2)
        return text
    }

    private fun getSearchParams(binding : FragmentAdvancedSearchBinding) : SearchParams {
        val restaurants : List<String>? = sharedViewModel.selectedRestaurant
        val foodType : List<String>? = sharedViewModel.selectedFoodTypes
        val favoritesOnly = if (binding.filterFavorites.isChecked) 1 else 0
        val minCalories : Int = getValue(binding.filterMinCalories.text.toString())
        val maxCalories : Int = getValue(binding.filterMaxCalories.text.toString())
        return SearchParams("",
            restaurants,
            foodType,
            favoritesOnly,
            minCalories,
            maxCalories)
    }

    private fun getValue(text : String) : Int {
        if (text.isBlank())
            return -1
        else
            return text.toInt()
    }


}
