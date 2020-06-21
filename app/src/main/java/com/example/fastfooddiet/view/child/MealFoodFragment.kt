package com.example.fastfooddiet.view.child

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.R
import com.example.fastfooddiet.adapters.FoodListAdapter
import com.example.fastfooddiet.databinding.FragmentChildListBinding
import com.example.fastfooddiet.view.MealFragmentDirections
import com.example.fastfooddiet.viewmodels.MealViewModel
import com.example.fastfooddiet.viewmodels.SharedViewModel

/*
 Child fragment for MealFragment's ViewPager
 */

class MealFoodFragment : Fragment() {
    companion object {
        private val KEY_ID = "mealId"

        fun newInstance(mealId: Int): MealFoodFragment {
            val args = Bundle()
            args.putInt(KEY_ID, mealId)

            val fragment = MealFoodFragment()
            fragment.arguments = args
            return fragment
        }
    }

    //**** PROPERTIES ****
    private lateinit var mealViewModel: MealViewModel
    private val sharedViewModel: SharedViewModel by navGraphViewModels(R.id.nav_meal)

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mealId = arguments?.getInt(KEY_ID) ?: error("No key found")

        //Get ViewModel
        mealViewModel = ViewModelProvider(this).get(MealViewModel::class.java).apply {
            setMeal(mealId)
        }

        val binding = FragmentChildListBinding
            .inflate(inflater, container, false).apply {
                setupRecyclerView(fragChildList, mealViewModel, sharedViewModel)
                setupDeleteMode(sharedViewModel, fragChildList)
            }

        return binding.root
    }

    //**** METHODS ****
    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        viewModel: MealViewModel,
        sharedModel: SharedViewModel
    ) {

        val onClick = { id: Int ->
            sharedModel.setDeleteMode(false)
            goToDetailFragment(id)
        }
        val onIconClick = { id: Int, position: Int, isFavorite: Boolean ->

            if (sharedModel.isDeleteMode()) {
                viewModel.meal.value?.mealFoods?.getOrNull(position)?.let {
                    viewModel.deleteMealFood(it.mealFoodId)
                    Toast.makeText(context, "Removed from meal", Toast.LENGTH_SHORT).show()
                }
                Unit
            } else {
                viewModel.setFavorite(id, isFavorite)

                val message = when (isFavorite) {
                    true -> "Removed from favorites"
                    false -> "Added to favorites"
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }

        val foodAdapter = FoodListAdapter(null, onClick,
            onIconClick, showItemDetailWithSize = true)
            .also { adapter ->
                viewModel.meal.observe(viewLifecycleOwner, Observer { meal ->
                    adapter.setData(meal.foods, sharedModel.isDeleteMode())
                })
            }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MealFoodFragment.activity)
            adapter = foodAdapter
        }
    }

    private fun setupDeleteMode(sharedModel: SharedViewModel, recyclerView: RecyclerView) {

        //When delete icon on toolbar is clicked, change meal item icon in the list
        sharedModel.isDeleteMode.observe(viewLifecycleOwner, Observer {isDeleteMode ->
            val adapter = recyclerView.adapter
            if (adapter is FoodListAdapter) {
                adapter.setDeleteIcon(isDeleteMode)
            }
        })
    }

    private fun goToDetailFragment(foodId: Int) {
        val action = MealFragmentDirections.toDetailFragment(foodId)
        findNavController().navigate(action)
    }
}
