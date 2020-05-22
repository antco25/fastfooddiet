package com.example.fastfooddiet.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.databinding.FragmentMainBinding
import com.example.fastfooddiet.view.CategoryFragment.CategoryType

class MainFragment : Fragment() {

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding
            .inflate(inflater, container, false).apply {
                fragment = this@MainFragment
                //setupRecyclerView(mainFragRecyclerView)
            }

        return binding.root
    }

    //**** METHODS ****
    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@MainFragment.activity, 2)
            //adapter =
        }
    }

    fun toCategoryListFragment() {
        val action = MainFragmentDirections
            .toCategoryFragment(
                "Select a Restaurant", CategoryType.RESTAURANT)
        findNavController().navigate(action)
    }

    fun toFoodListFragment() {
        val action = MainFragmentDirections
            .toFoodListFragment(header ="Search Results")
        findNavController().navigate(action)
    }

    fun toSettingsFragment() {
        val action = MainFragmentDirections.toSettingsFragment()
        findNavController().navigate(action)
    }
}


//TODO: Delete
/*
    private fun setupListeners(binding : FragmentMain2Binding) {

        binding.mainFragSearchView.setOnClickListener {
            val action = MainFragmentDirections
                .toFoodListFragment("Search Menu Items",
                    expandSearchView = true)
            findNavController().navigate(action)
        }

        binding.mainFragButtonRestaurants.setOnClickListener {
            val action = MainFragmentDirections
                .toCategoryListFragment(
                    "Browse By Restaurant", CategoryType.RESTAURANT)
            findNavController().navigate(action)
        }

        binding.mainFragButtonFoodType.setOnClickListener {
            val action = MainFragmentDirections
                .toCategoryListFragment(
                    "Browse By Food Type", CategoryType.FOOD_TYPE)
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
 */