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

class FoodTypeFragment : Fragment() {

    //**** PROPERTIES ****
    private lateinit var categoryViewModel: CategoryViewModel
    private val args: FoodTypeFragmentArgs by navArgs()

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Get ViewModel
        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
            .apply { setRestaurant.value = args.restaurant }

        val binding = FragmentCategoryBinding
            .inflate(inflater, container, false).apply {
                viewModel = categoryViewModel
                lifecycleOwner = viewLifecycleOwner
                setupHeader(args.restaurant, categoryViewModel)
                setupToolBar(activity as AppCompatActivity, catFragToolbar)
                setupRecyclerView(catFragRecyclerView, categoryViewModel, args.restaurant)
            }

        return binding.root
    }

    //**** METHODS ****
    private fun setupToolBar(activity: AppCompatActivity, toolbar: Toolbar) {
        activity.setSupportActionBar(toolbar)

        //Set back button to HomeFragment
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }


    private fun setupRecyclerView(recyclerView: RecyclerView,
                                  categoryViewModel: CategoryViewModel,
                                  selectedRestaurant: String) {

        val onItemClick: (String) -> Unit = { selectedFoodType ->
            toFoodListFragment(selectedRestaurant, selectedFoodType)
        }

        val viewAdapter = CategoryAdapter(onItemClick)
            .also { categoryAdapter ->

                //Set live data observers
                categoryViewModel.foodTypes.observe(viewLifecycleOwner,
                    Observer { categoryAdapter.setData(it) })

            }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@FoodTypeFragment.activity, 3)
            adapter = viewAdapter
        }
    }

    private fun setupHeader(selectedRestaurant : String, viewModel: CategoryViewModel) {
        viewModel.header.value = "$selectedRestaurant - Select Category"
    }

    private fun toFoodListFragment(restaurant: String, foodType: String) {
        val header = "$restaurant - $foodType"
        val mode = FoodListMode.BROWSE
        val browseParams = BrowseParams(restaurant, foodType)

        val action = FoodTypeFragmentDirections
            .toFoodListFragment(header, mode, browseParams = browseParams)

        findNavController().navigate(action)
    }

}