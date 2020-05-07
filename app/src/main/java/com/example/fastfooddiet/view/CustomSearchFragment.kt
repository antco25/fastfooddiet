package com.example.fastfooddiet.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import com.example.fastfooddiet.R
import com.example.fastfooddiet.databinding.FragmentCustomSearchBinding
import com.example.fastfooddiet.viewmodels.CustomSearchViewModel
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.fastfooddiet.data.CategoryFilter
import com.example.fastfooddiet.viewcomponent.SearchFilterLayout

class CustomSearchFragment : Fragment() {

    //TODO: Weird animation when view is initially not expanded then you expand it
    private val customSearchViewModel : CustomSearchViewModel by navGraphViewModels(R.id.nav_graph) ////TODO: Change to inner nav graph

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
                setCategoryFilters(csearchRestAdd, csearchRestLayout,
                    csearchFoodTypeAdd, csearchFoodTypeLayout)
                setNutritionData(this)
                button.setOnClickListener {
                    Log.d("xfast", "Calorie min: " + customSearchViewModel.getNutritionData("calorieMin") + " max: " + customSearchViewModel.getNutritionData("calorieMax"))
                }
            }

        return binding.root
    }

    //**** METHODS ****

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
                            getRestaurants().isCheckedItems)
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
                            getFoodTypes().isCheckedItems, false)
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

    //Possible to reduce code length?
    private fun setNutritionData(binding : FragmentCustomSearchBinding) {
        binding.apply {
            customSearchViewModel.apply {
                nutritionData.observe(viewLifecycleOwner, Observer {
                    if (it["calorieMin"] == 0)
                        csearchCaloriesMin.text = ""
                    else
                        csearchCaloriesMin.text = it["calorieMin"].toString()

                    if (it["calorieMax"] == -1)
                        csearchCaloriesMax.text = ""
                    else
                        csearchCaloriesMax.text = it["calorieMax"].toString()
                })
            }
        }
    }

    fun openNumberSelectDialog(maxKey : String, minKey : String, isMax: Boolean) {
        val action = CustomSearchFragmentDirections
            .toNumberSelectDialogFragment(maxKey, minKey, isMax)
        findNavController().navigate(action)
    }
}

