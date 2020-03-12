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
        val binding = FragmentMainBinding.inflate(inflater, container, false)
        setupListeners(binding)
        return binding.root
    }

    //**** METHODS ****
    private fun setupListeners(binding : FragmentMainBinding) {

        //TODO: Change color to match normal searchview, merge this with setupListeners function
        binding.mainFragSearchView.setOnClickListener {
            val action = MainFragmentDirections
                .actionMainFragmentToFoodListFragment("Search Menu Items",
                    showKeyboardOnEnter = true)
            findNavController().navigate(action)
        }

        binding.mainFragButtonRestaurants.setOnClickListener {
            val action = MainFragmentDirections
                .actionMainFragmentToCategoryListFragment(
                    "Browse By Restaurant", Category.RESTAURANT)
            findNavController().navigate(action)
        }

        binding.mainFragButtonFoodType.setOnClickListener {
            val action = MainFragmentDirections
                .actionMainFragmentToCategoryListFragment(
                    "Browse By Food Type", Category.FOOD_TYPE)
            findNavController().navigate(action)
        }

        binding.mainFragButtonAdvancedSearch.setOnClickListener {
            val action = MainFragmentDirections
                .actionMainFragmentToAdvancedSearchFragment()
            findNavController().navigate(action)
        }

        binding.mainFragButtonFavorites.setOnClickListener {
            val action = MainFragmentDirections
                .actionMainFragmentToFoodListFragment("Browse Favorites",
                    true,true, false)
            findNavController().navigate(action)
        }
    }
}