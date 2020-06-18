package com.example.fastfooddiet.view.child

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.example.fastfooddiet.adapters.MealListAdapter
import com.example.fastfooddiet.databinding.FragmentChildListBinding
import com.example.fastfooddiet.databinding.GenericEmptyResultBinding
import com.example.fastfooddiet.view.FavoriteFragmentDirections
import com.example.fastfooddiet.viewmodels.FavoriteViewModel
import com.example.fastfooddiet.viewmodels.SharedViewModel

/*
 Child fragment for FavoriteFragment's ViewPager
 Fragment 0 - List of Favorite Foods
 Fragment 1 - List of Meals
 */

class FavoriteChildFragment : Fragment() {
    companion object {
        private val KEY_POSITION = "position"

        fun newInstance(position: Int): FavoriteChildFragment {
            val args = Bundle()
            args.putInt(KEY_POSITION, position)

            val fragment = FavoriteChildFragment()
            fragment.arguments = args
            return fragment
        }
    }

    //**** PROPERTIES ****
    private lateinit var favoriteViewModel: FavoriteViewModel
    private val sharedViewModel: SharedViewModel by navGraphViewModels(R.id.nav_favorites)

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Get ViewModel
        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        val fragmentPosition = arguments?.getInt(KEY_POSITION) ?: error("No key found")
        val listAdapter = when (fragmentPosition) {
            0 -> getFoodAdapter(favoriteViewModel)
            else -> getMealAdapter(favoriteViewModel, sharedViewModel)
        }

        val binding = FragmentChildListBinding
            .inflate(inflater, container, false).apply {
                fragChildList.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(this@FavoriteChildFragment.activity)
                    adapter = listAdapter
                }

                if (fragmentPosition == 0) {
                    setupEmptyFoodResult(fragChildListEmpty, favoriteViewModel)
                } else {
                    setupEmptyMealResult(fragChildListEmpty, favoriteViewModel)
                    setupDeleteMode(sharedViewModel, fragChildList)
                }
            }

        return binding.root
    }

    //**** METHODS ****
    private fun getFoodAdapter(viewModel: FavoriteViewModel):
            RecyclerView.Adapter<out RecyclerView.ViewHolder> {

        val onClick = { id: Int ->
            goToDetailFragment(id)
        }

        val onIconClick = { id: Int, _: Int, isFavorite: Boolean ->
            viewModel.setFavorite(id, isFavorite)

            val message = when (isFavorite) {
                true -> "Removed from favorites"
                false -> "Added to favorites"
            }

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        return FoodListAdapter(
            null, onClick,
            onIconClick, showItemDetailWithSize = true
        )
            .also { adapter ->
                viewModel.favoriteFoods.observe(viewLifecycleOwner, Observer {
                    adapter.setData(it, null)
                })
            }
    }

    private fun getMealAdapter(viewModel: FavoriteViewModel,
                               sharedModel: SharedViewModel): MealListAdapter {

        val mealOnClick = { id: Int ->
            sharedModel.setDeleteMode(false)
            goToMealFragment(id)
        }
        val mealIconOnClick = { id: Int ->
            if (sharedViewModel.isDeleteMode()) {
                favoriteViewModel.deleteMeal(id)
                Toast.makeText(context, "Meal deleted", Toast.LENGTH_SHORT).show()
            }
        }

        return MealListAdapter(null, mealOnClick, mealIconOnClick)
            .also { adapter ->
                viewModel.meals.observe(viewLifecycleOwner, Observer {
                    adapter.setData(it, sharedModel.isDeleteMode())
                })
            }
    }

    private fun setupDeleteMode(sharedModel: SharedViewModel, recyclerView: RecyclerView) {

        //When delete icon on toolbar is clicked, change meal item icon in the list
        sharedModel.isDeleteMode.observe(viewLifecycleOwner, Observer {isDeleteMode ->
            val adapter = recyclerView.adapter
            if (adapter is MealListAdapter) {
                adapter.changeIcon(isDeleteMode)
            }
        })

    }

    private fun goToDetailFragment(foodId: Int) {
        val action = FavoriteFragmentDirections.toDetailFragment(foodId)
        findNavController().navigate(action)
    }

    private fun goToMealFragment(mealId: Int) {
        val action = FavoriteFragmentDirections.toMealFragment(mealId)
        findNavController().navigate(action)
    }

    private fun setupEmptyFoodResult(foodLayout: GenericEmptyResultBinding,
                                     viewModel: FavoriteViewModel) {
        foodLayout.apply {
            emptyResultImage.setImageResource(R.drawable.ic_star_border)
            emptyResultHeader.setText(R.string.empty_fav_food_header)
            emptyResultText.setText(R.string.empty_fav_food_text)
        }

        viewModel.favoriteFoods.observe(viewLifecycleOwner, Observer {
            viewModel.isFoodListEmpty = it.isNullOrEmpty()

            if (viewModel.isFoodListEmpty)
                foodLayout.root.visibility = View.VISIBLE
            else
                foodLayout.root.visibility = View.INVISIBLE
        })
    }

    private fun setupEmptyMealResult(mealLayout: GenericEmptyResultBinding,
                                     viewModel: FavoriteViewModel) {

        mealLayout.apply {
            emptyResultImage.setImageResource(R.drawable.ic_restaurant_menu)
            emptyResultHeader.setText(R.string.empty_fav_meal_header)
            emptyResultText.setText(R.string.empty_fav_meal_text)
        }

        viewModel.meals.observe(viewLifecycleOwner, Observer {
            viewModel.isMealListEmpty = it.isNullOrEmpty()

            if (viewModel.isMealListEmpty)
                mealLayout.root.visibility = View.VISIBLE
            else
                mealLayout.root.visibility = View.INVISIBLE
        })
    }


}
