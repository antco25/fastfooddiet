package com.example.fastfooddiet.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.adapters.CategoryAdapter
import com.example.fastfooddiet.data.BrowseParams
import com.example.fastfooddiet.data.SearchParams
import com.example.fastfooddiet.databinding.FragmentCategoryBinding
import com.example.fastfooddiet.viewmodels.CategoryViewModel

open class CategoryFragment : Fragment() {

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

        val binding = FragmentCategoryBinding
            .inflate(inflater, container, false).apply {
                viewModel = categoryViewModel
                lifecycleOwner = viewLifecycleOwner
                setupToolBar(activity as AppCompatActivity, catFragToolbar, categoryViewModel)
                setupRecyclerView(catFragRecyclerView, categoryViewModel)
            }

        return binding.root
    }

    //**** METHODS ****
    private fun setupToolBar(activity: AppCompatActivity, toolbar: Toolbar,
                             categoryViewModel: CategoryViewModel) {
        activity.setSupportActionBar(toolbar)

        //Set back button to MainFragment
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Flow:
        //[Navigate Up] - [Restaurant] - [Food Type] - [Next Fragment]
        toolbar.setNavigationOnClickListener {
            categoryViewModel.apply {
                if (getCategory() == CategoryType.RESTAURANT)
                    findNavController().navigateUp()
                else
                    setCategory(CategoryType.RESTAURANT)
            }
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView,
                                  categoryViewModel: CategoryViewModel) {

        val onItemClick : (String) -> Unit = { selected ->
            categoryViewModel.apply {
                if (getCategory() == CategoryType.RESTAURANT)
                    setRestaurant(selected)
                else {
                    toFoodListFragment(selectedRestaurant!!, selected)
                }
            }
        }

        val viewAdapter = CategoryAdapter(onItemClick)
            .also { categoryAdapter ->

                //Set live data observers
                categoryViewModel.categories.observe(viewLifecycleOwner,
                    Observer { categoryAdapter.setData(it) })

            }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@CategoryFragment.activity,3)
            adapter = viewAdapter
        }
    }

    private fun toFoodListFragment(restaurant : String, foodType : String) {
        val header = "$restaurant - $foodType"
        val mode = FoodListMode.BROWSE
        val browseParams = BrowseParams(restaurant, foodType)

        val action = CategoryFragmentDirections
            .toFoodListFragment(header, mode, browseParams = browseParams)

        findNavController().navigate(action)
    }

    enum class CategoryType {
        RESTAURANT,
        FOOD_TYPE
    }
}