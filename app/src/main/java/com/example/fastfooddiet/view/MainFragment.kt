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
            }

        return binding.root
    }

    //**** METHODS ****
    fun toCategoryListFragment() {
        val action = MainFragmentDirections
            .toCategoryFragment(
                "Select a Restaurant", CategoryType.RESTAURANT)
        findNavController().navigate(action)
    }

    fun toFoodListFragment() {
        val action = MainFragmentDirections
            .toFoodListFragment()
        findNavController().navigate(action)
    }

    fun toSettingsFragment() {
        val action = MainFragmentDirections.toSettings()
        findNavController().navigate(action)
    }

    fun toCustomSearchFragment() {
        val action = MainFragmentDirections.toCustomSearch()
        findNavController().navigate(action)
    }

    fun toFavoriteFragment() {
        val action = MainFragmentDirections.toFavorites()
        findNavController().navigate(action)
    }


}
