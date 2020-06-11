package com.example.fastfooddiet.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import com.example.fastfooddiet.R
import com.example.fastfooddiet.databinding.FragmentCustomSearchBinding
import com.example.fastfooddiet.viewmodels.CustomSearchViewModel
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.fastfooddiet.data.CategoryFilter
import com.example.fastfooddiet.data.SearchParams
import com.example.fastfooddiet.viewcomponent.SearchFilterLayout

class CustomSearchFragment : Fragment() {

    //TODO: Weird animation when view is initially not expanded then you expand it
    private val customSearchViewModel : CustomSearchViewModel by navGraphViewModels(R.id.nav_custom_search)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentCustomSearchBinding
            .inflate(inflater, container, false)
            .apply {
                viewmodel = customSearchViewModel
                lifecycleOwner = viewLifecycleOwner
                fragment = this@CustomSearchFragment
                setupToolBar(activity as AppCompatActivity, csearchFragToolbar)
                setCategoryFilters(csearchRestAdd, csearchRestLayout,
                    csearchFoodTypeAdd, csearchFoodTypeLayout)
            }

        return binding.root
    }

    //**** METHODS ****
    private fun setupToolBar(activity: AppCompatActivity, toolbar: Toolbar) {
        activity.setSupportActionBar(toolbar)

        //Set back button to MainFragment
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setCategoryFilters(restButton : View, restLayout: SearchFilterLayout,
                                   foodTypeButton : View, foodTypeLayout: SearchFilterLayout) {

        customSearchViewModel.apply {

            /*
            Set restaurant filter items and button onClick
            */
            restaurantItems.observe(viewLifecycleOwner, Observer { items ->

                if (getRestaurants().isEmpty())
                    updateRestaurants(CategoryFilter(items))

                restButton.setOnClickListener {
                    val action = CustomSearchFragmentDirections
                        .toCategorySelectDialogFragment(getRestaurants().items,
                            getRestaurants().getIsCheckedItemsForDialog())
                    findNavController().navigate(action)
                }
            })

            /*
            When checked restaurant filters are changed, update restaurant filter views
            */
            restaurants.observe(viewLifecycleOwner, Observer { restaurants ->
                restLayout.setCategoryViews(restaurants.getCheckedItemsForView(), layoutInflater)
            })

            /*
            Set food type filter items and button onClick
            */
            foodTypeItems.observe(viewLifecycleOwner, Observer { items ->

                if (getFoodTypes().isEmpty())
                    updateFoodTypes(CategoryFilter(items))

                foodTypeButton.setOnClickListener {
                    val action = CustomSearchFragmentDirections
                        .toCategorySelectDialogFragment(getFoodTypes().items,
                            getFoodTypes().getIsCheckedItemsForDialog(), false)
                    findNavController().navigate(action)
                }
            })

            /*
            When checked food type filters are changed, update restaurant filter views
            */
            foodTypes.observe(viewLifecycleOwner, Observer { foodTypes ->
                foodTypeLayout.setCategoryViews(foodTypes.getCheckedItemsForView(), layoutInflater)
            })
        }
    }

    fun minText(value : Float, isFloat : Boolean) : String {
        return when {
            value == 0f -> ""
            isFloat -> value.toString()
            else -> value.toInt().toString()
        }
    }

    fun maxText(value : Float, isFloat : Boolean) : String {
        return when {
            value == -1f -> ""
            isFloat -> value.toString()
            else -> value.toInt().toString()
        }
    }

    fun openNumberSelectDialog(maxKey : String, minKey : String, isMax: Boolean, isFloat: Boolean) {
        val action = CustomSearchFragmentDirections
            .toNumberSelectDialogFragment(maxKey, minKey, isMax = isMax, isFloat = isFloat)
        findNavController().navigate(action)
    }

    fun goToFoodListFragment() {
        val mode = FoodListMode.CUSTOM
        val searchParams = customSearchViewModel.getSearchParams()

        val action = CustomSearchFragmentDirections
            .toFoodListFragment(mode = mode, searchParams = searchParams)
        findNavController().navigate(action)
    }
}

