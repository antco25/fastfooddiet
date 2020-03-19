package com.example.fastfooddiet.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fastfooddiet.data.SearchParams
import com.example.fastfooddiet.databinding.FragmentMainBinding
import com.example.fastfooddiet.view.CategoryListFragment.Category

class MainFragment : Fragment() {

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding
            .inflate(inflater, container, false).apply { setupListeners(this) }

        return binding.root
    }

    //**** METHODS ****
    private fun setupListeners(binding : FragmentMainBinding) {

        binding.mainFragSearchView.setOnClickListener {
            val action = MainFragmentDirections
                .toFoodListFragment("Search Menu Items",
                    showKeyboardOnEnter = true)
            findNavController().navigate(action)
        }

        binding.mainFragButtonRestaurants.setOnClickListener {
            val action = MainFragmentDirections
                .toCategoryListFragment(
                    "Browse By Restaurant", Category.RESTAURANT)
            findNavController().navigate(action)
        }

        binding.mainFragButtonFoodType.setOnClickListener {
            val action = MainFragmentDirections
                .toCategoryListFragment(
                    "Browse By Food Type", Category.FOOD_TYPE)
            findNavController().navigate(action)
        }

        binding.mainFragButtonAdvancedSearch.setOnClickListener {
            val action = MainFragmentDirections
                .toAdvancedSearchFragment()
            findNavController().navigate(action)
        }

        binding.mainFragButtonFavorites.setOnClickListener {
            val action = MainFragmentDirections
                .toFoodListFragment("Browse Favorites",
                    true,true, false)
            findNavController().navigate(action)
        }
    }
}