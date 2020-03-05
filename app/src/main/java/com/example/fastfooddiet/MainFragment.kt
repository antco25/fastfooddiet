package com.example.fastfooddiet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fastfooddiet.databinding.FragmentMainBinding
import com.example.fastfooddiet.SearchListFragment.SearchType

class MainFragment : Fragment() {

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater, container, false)
        setupSearchView(binding.mainFragSearchView)
        setupButtons(binding)
        return binding.root
    }

    //**** METHODS ****
    private fun setupSearchView(view : View) {
        //TODO: Change color to match normal searchview, merge this with setupButtons function
        view.setOnClickListener {
            val header = "Search All Menu Items"
            navigateToSearchListFragment(header, SearchType.FOOD)
        }
    }

    private fun setupButtons(binding : FragmentMainBinding) {
        binding.mainFragButtonRestaurants.setOnClickListener {
            val header = "Browse By Restaurant"
            navigateToSearchListFragment(header, SearchType.RESTAURANT)
        }

        binding.mainFragButtonFoodType.setOnClickListener {
            val header = "Browse By Food Type"
            navigateToSearchListFragment(header, SearchType.FOOD_TYPE)
        }
        binding.mainFragButtonAdvancedSearch.setOnClickListener {
            //TODO: Advanced Search
        }

        binding.mainFragButtonFavorites.setOnClickListener {
            val header = "Browse Favorites"
            navigateToSearchListFragment(header, SearchType.FAVORITE)
        }
    }

    private fun navigateToSearchListFragment(header : String,
                                             searchType : SearchType) {
        val action = MainFragmentDirections
            .actionMainFragmentToSearchListFragment(header, searchType)
        findNavController().navigate(action)
    }
}