package com.example.fastfooddiet.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.R
import com.example.fastfooddiet.adapters.FoodListAdapter
import com.example.fastfooddiet.adapters.MealListAdapter
import com.example.fastfooddiet.databinding.FragmentDetailBinding
import com.example.fastfooddiet.databinding.FragmentFavoriteBinding
import com.example.fastfooddiet.databinding.GenericEmptyResultBinding
import com.example.fastfooddiet.viewmodels.DetailViewModel
import com.example.fastfooddiet.viewmodels.FavoriteViewModel
import com.example.fastfooddiet.viewmodels.SharedViewModel

class FavoriteFragment : Fragment() {

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

        setupSharedViewModel()

        val binding = FragmentFavoriteBinding.inflate(inflater, container, false)
            .apply {
                fragment = this@FavoriteFragment
                viewmodel = favoriteViewModel
                lifecycleOwner = viewLifecycleOwner
                setupToolBar(activity as AppCompatActivity, favFragToolbar)
                setupRecyclerView(favFragRecyclerview, favoriteViewModel)
                favoriteViewModel.isDeleteMode.observe(viewLifecycleOwner, Observer {
                    isDeleteModeChange(it, favFragDelMealButton, favFragRecyclerview)
                })
                setupEmptyResult(favFragFoodEmpty, favFragMealEmpty, favoriteViewModel)
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

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        viewModel: FavoriteViewModel
    ) {

        val mealOnClick = { id: Int ->
            goToMealFragment(id)
            favoriteViewModel.setDeleteMode(false)
        }
        val mealIconOnClick = { id: Int ->
            if (favoriteViewModel.isDeleteMode()) {
                favoriteViewModel.deleteMeal(id)
                Toast.makeText(context, "Meal deleted", Toast.LENGTH_SHORT).show()
            }
        }

        val mealAdapter = MealListAdapter(null, mealOnClick, mealIconOnClick)
            .also { adapter ->
                viewModel.meals.observe(viewLifecycleOwner, Observer {
                    adapter.setData(it, viewModel.isDeleteMode())
                })
            }

        val onClick = { id: Int -> goToDetailFragment(id) }
        val onIconClick = { id: Int, position: Int, isFavorite: Boolean ->
            viewModel.setFavorite(id, isFavorite)

            val message = when (isFavorite) {
                true -> "Removed from favorites"
                false -> "Added to favorites"
            }

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        val foodAdapter = FoodListAdapter(null, onClick, onIconClick)
            .also { adapter ->
                viewModel.favoriteFoods.observe(viewLifecycleOwner, Observer {
                    adapter.setData(it, null)
                })
            }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@FavoriteFragment.activity)

            viewModel.isFavoriteFoods.observe(viewLifecycleOwner, Observer { isFavoriteFoods ->
                if (isFavoriteFoods) {
                    adapter = foodAdapter
                    favoriteViewModel.setDeleteMode(false)
                } else {
                    adapter = mealAdapter
                    favoriteViewModel.setDeleteMode(favoriteViewModel.isDeleteMode())
                }
            })
        }
    }

    private fun setupSharedViewModel() {
        sharedViewModel.textInput.observe(viewLifecycleOwner, Observer { name ->
            if (sharedViewModel.textChanged && name.isNotBlank()) {
                favoriteViewModel.addMeal(name)
                sharedViewModel.textChanged = false
            }
        })
    }

    private fun goToDetailFragment(foodId: Int) {
        val action = FavoriteFragmentDirections.toDetailFragment(foodId)
        findNavController().navigate(action)
    }

    private fun goToMealFragment(mealId: Int) {
        val action = FavoriteFragmentDirections
            .toMealFragment(mealId)
        findNavController().navigate(action)
    }

    fun goToAddMealDialog() {
        favoriteViewModel.setDeleteMode(false)
        val action = FavoriteFragmentDirections.toTextInputDialog(
            R.id.nav_favorites,
            "Create a New Meal"
        )
        findNavController().navigate(action)
    }

    private fun isDeleteModeChange(
        isDelete: Boolean,
        deleteIcon: ImageView,
        recyclerView: RecyclerView
    ) {

        val adapter = recyclerView.adapter

        if (adapter is MealListAdapter) {
            //Change meal adapter
            adapter.changeIcon(isDelete)

            //Change icon picture
            if (isDelete)
                deleteIcon.setImageResource(R.drawable.ic_delete_cancel)
            else
                deleteIcon.setImageResource(R.drawable.ic_delete)
        }
    }

    private fun setupEmptyResult(foodLayout: GenericEmptyResultBinding,
                                 mealLayout: GenericEmptyResultBinding,
                                 viewModel: FavoriteViewModel) {
        foodLayout.apply {
            emptyResultImage.setImageResource(R.drawable.ic_star_border)
            emptyResultHeader.setText(R.string.empty_fav_food_header)
            emptyResultText.setText(R.string.empty_fav_food_text)
        }

        mealLayout.apply {
            emptyResultImage.setImageResource(R.drawable.ic_restaurant_menu)
            emptyResultHeader.setText(R.string.empty_fav_meal_header)
            emptyResultText.setText(R.string.empty_fav_meal_text)
        }

        viewModel.isFavoriteFoods.observe(viewLifecycleOwner, Observer { isFavoriteFoods ->
            if (isFavoriteFoods && viewModel.isFoodListEmpty) {
                foodLayout.root.visibility = View.VISIBLE
                mealLayout.root.visibility = View.INVISIBLE
            } else if (!isFavoriteFoods && viewModel.isMealListEmpty){
                foodLayout.root.visibility = View.INVISIBLE
                mealLayout.root.visibility = View.VISIBLE
            }
            else {
                foodLayout.root.visibility = View.INVISIBLE
                mealLayout.root.visibility = View.INVISIBLE
            }
        })

        viewModel.favoriteFoods.observe(viewLifecycleOwner, Observer {
            viewModel.isFoodListEmpty = it.isNullOrEmpty()

            if (viewModel.isFavoriteFoods.value!! && viewModel.isFoodListEmpty)
                foodLayout.root.visibility = View.VISIBLE
            else
                foodLayout.root.visibility = View.INVISIBLE
        })

        viewModel.meals.observe(viewLifecycleOwner, Observer {
            viewModel.isMealListEmpty = it.isNullOrEmpty()

            if (!viewModel.isFavoriteFoods.value!! && viewModel.isMealListEmpty)
                mealLayout.root.visibility = View.VISIBLE
            else
                mealLayout.root.visibility = View.INVISIBLE
        })
    }

}