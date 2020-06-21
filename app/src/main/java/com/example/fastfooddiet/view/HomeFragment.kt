package com.example.fastfooddiet.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.adapters.CategoryAdapter
import com.example.fastfooddiet.databinding.FragmentHomeBinding
import com.example.fastfooddiet.viewmodels.CategoryViewModel

class HomeFragment : Fragment() {

    //**** PROPERTIES ****
    private lateinit var categoryViewModel: CategoryViewModel

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Get ViewModel
        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)

        val binding = FragmentHomeBinding
            .inflate(inflater, container, false).apply {
                setupRecyclerView(homeFragRecyclerView, categoryViewModel)
                setupSearchView(homeFragSearchView)
            }

        return binding.root
    }

    //**** METHODS ****
    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        categoryViewModel: CategoryViewModel
    ) {

        val onItemClick: (String) -> Unit = { selectedRestaurant ->
            toFoodTypeFragment(selectedRestaurant)
        }

        val viewAdapter = CategoryAdapter(onItemClick)
            .also { categoryAdapter ->

                //Set live data observers
                categoryViewModel.restaurants.observe(viewLifecycleOwner,
                    Observer { categoryAdapter.setData(it) })

            }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@HomeFragment.activity, 3)
            adapter = viewAdapter
        }
    }

    private fun setupSearchView(searchView: View) {
        searchView.setOnClickListener{
            val action = HomeFragmentDirections
                .toFoodListFragment()
            findNavController().navigate(action)
        }
    }

    private fun toFoodTypeFragment(selectedRestaurant : String) {
        val action = HomeFragmentDirections
            .toFoodTypeFragment(selectedRestaurant)

        findNavController().navigate(action)
    }


}
