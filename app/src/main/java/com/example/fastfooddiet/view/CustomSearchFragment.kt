package com.example.fastfooddiet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import com.example.fastfooddiet.R
import com.example.fastfooddiet.databinding.FragmentCustomSearchBinding
import com.example.fastfooddiet.viewmodels.CustomSearchViewModel
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.fastfooddiet.viewcomponent.SearchFilterLayout

class CustomSearchFragment : Fragment() {

    val customSearchViewModel : CustomSearchViewModel by navGraphViewModels(R.id.nav_graph) ////TODO: Change to inner nav graph

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCustomSearchBinding
            .inflate(inflater, container, false)
            .apply {
                setExpandButton(csearchExpandRest, csearchGroupRest)
                setExpandButton(csearchExpandFoodType, csearchGroupFoodType)
                setCategoryFilters(csearchRestAdd, csearchRestLayout,
                    csearchFoodTypeAdd, csearchFoodTypeLayout)
            }

        return binding.root
    }

    //**** METHODS ****
    private fun setExpandButton(button : View, group: Group) {
        button.setOnClickListener {
            if (group.visibility != View.VISIBLE)
                group.visibility = View.VISIBLE
            else
                group.visibility = View.GONE
        }
    }

    private fun setCategoryFilters(restButton : View, restLayout: SearchFilterLayout,
                                   foodTypeButton : View, foodTypeLayout: SearchFilterLayout) {

        /*
        Set restaurant filter button onClick
        */
        customSearchViewModel.restaurants.observe(viewLifecycleOwner, Observer { items ->
            val checkedRestaurants =
                customSearchViewModel.getCheckedRestaurants(items.size)

            restButton.setOnClickListener {
                val action = CustomSearchFragmentDirections
                    .toCategorySelectDialogFragment(items, checkedRestaurants)
                findNavController().navigate(action)
            }
        })

        /*
        When checked restaurant filters are changed, update restaurant filter views
         */
        customSearchViewModel.checkedRestaurants.observe(viewLifecycleOwner,
            Observer { checkedItems ->
                if (checkedItems[0]) {
                    restLayout.setCategoryViews(listOf("All"), layoutInflater)
                }
                else {
                    val checkedItemsList = mutableListOf<String>()
                    customSearchViewModel.restaurants.value?.mapIndexed { index, name ->
                        if (checkedItems[index])
                            checkedItemsList.add(name)
                    }
                    restLayout.setCategoryViews(checkedItemsList, layoutInflater)
                }
        })

        /*
        Set food type filter button onClick
        */
        customSearchViewModel.foodTypes.observe(viewLifecycleOwner, Observer { items ->
            val checkedFoodTypes = customSearchViewModel.getCheckedFoodTypes(items.size)

            foodTypeButton.setOnClickListener {
                val action = CustomSearchFragmentDirections
                    .toCategorySelectDialogFragment(items, checkedFoodTypes, false)
                findNavController().navigate(action)
            }
        })

        /*
        When checked food type filters are changed, update food type filter views
         */
        customSearchViewModel.checkedFoodTypes.observe(viewLifecycleOwner,
            Observer { checkedItems ->

                if (checkedItems[0]) {
                    foodTypeLayout.setCategoryViews(listOf("All"), layoutInflater)
                }

                else {
                    val checkedItemsList = mutableListOf<String>()
                    customSearchViewModel.foodTypes.value?.mapIndexed { index, name ->
                        if (checkedItems[index])
                            checkedItemsList.add(name)
                    }

                    foodTypeLayout.setCategoryViews(checkedItemsList, layoutInflater)
                }
            })
    }
}

