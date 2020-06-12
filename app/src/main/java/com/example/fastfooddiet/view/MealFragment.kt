package com.example.fastfooddiet.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
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
import com.example.fastfooddiet.data.MealData
import com.example.fastfooddiet.databinding.FragmentMealBinding
import com.example.fastfooddiet.databinding.GenericEmptyResultBinding
import com.example.fastfooddiet.viewmodels.MealViewModel
import com.example.fastfooddiet.viewmodels.SharedViewModel

class MealFragment : Fragment() {

    //**** PROPERTIES ****
    private lateinit var viewModel: MealViewModel
    private val sharedViewModel: SharedViewModel by navGraphViewModels(R.id.nav_meal)
    private val args: MealFragmentArgs by navArgs()

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Get ViewModel
        viewModel = ViewModelProvider(this).get(MealViewModel::class.java).apply {
            setMeal(args.mealId)
            isCustomNutritionData.value = isCustomData(context)
        }

        setupSharedViewModel()

        val binding = FragmentMealBinding.inflate(inflater, container, false)
            .apply {
                viewmodel = viewModel
                fragment = this@MealFragment
                lifecycleOwner = viewLifecycleOwner
                setupToolBar(activity as AppCompatActivity, mealFragToolbar)
                setupRecyclerView(mealFragList, viewModel)
                viewModel.isDeleteMode.observe(viewLifecycleOwner, Observer {
                    isDeleteModeChange(it, mealFragDelete, mealFragList)
                })
                setupEmptyResult(mealFragEmpty)
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

    private fun isCustomData(context: Context?): Boolean {
        context?.apply {
            val sharedPref = getSharedPreferences(
                resources.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            )

            val value = sharedPref.getInt(
                resources
                    .getString(R.string.nutrition_key), 0
            )
            return (value == 1)
        }
        return false
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        viewModel: MealViewModel
    ) {

        val onClick = { id: Int -> goToDetailFragment(id)}
        val onIconClick = { _: Int, position: Int, _: Boolean ->
            if (viewModel.isDeleteMode()) {
                viewModel.mealFoods?.getOrNull(position)?.let {
                    viewModel.deleteMealFood(it.mealFoodId)
                    showToast("Removed from meal")
                }
            }
        }

        val foodAdapter = FoodListAdapter(null, onClick,
            onIconClick, showItemDetailWithSize = true)
            .also { adapter ->
                viewModel.meal.observe(viewLifecycleOwner, Observer { meal ->
                    adapter.setData(meal.foods, viewModel.isDeleteMode())
                    viewModel.mealFoods = meal.mealFoods
                    viewModel.isMealEmpty.value = meal.foods.isNullOrEmpty()
                })
            }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MealFragment.activity)
            adapter = foodAdapter
        }
    }

    fun onViewButtonClick(isFoodView: Boolean) {
        viewModel.apply {
            this.isFoodView.value = isFoodView
            setDeleteMode(false)
        }
    }

    private fun goToDetailFragment(foodId: Int) {
        viewModel.setDeleteMode(false)
        val action = MealFragmentDirections.toDetailFragment(foodId)
        findNavController().navigate(action)
    }

    private fun isDeleteModeChange(
        isDelete: Boolean,
        deleteIcon: ImageView,
        recyclerView: RecyclerView
    ) {

        val adapter = recyclerView.adapter as FoodListAdapter

        //Change meal adapter
        adapter.changeIcon(isDelete)

        //Change icon picture
        if (isDelete)
            deleteIcon.setImageResource(R.drawable.ic_delete_cancel)
        else
            deleteIcon.setImageResource(R.drawable.ic_delete)
    }

    private fun setupSharedViewModel() {
        sharedViewModel.textInput.observe(viewLifecycleOwner, Observer { name ->
            if (sharedViewModel.textChanged && name.isNotBlank()) {
                sharedViewModel.textChanged = false

                //Update meal name
                viewModel.updateMeal(MealData(args.mealId, name))
                showToast("Meal updated")

            }
        })
    }

    fun goToUpdateNameDialog() {
        viewModel.setDeleteMode(false)
        val action = MealFragmentDirections
            .toTextInputMealDialog(R.id.nav_meal, "Rename meal")
        findNavController().navigate(action)
    }

    fun showToast(message : String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupEmptyResult(layout: GenericEmptyResultBinding) {
        layout.apply {
            emptyResultImage.setImageResource(R.drawable.ic_restaurant_menu)
            emptyResultHeader.setText(R.string.empty_meal_meal_header)
            emptyResultText.setText(R.string.empty_meal_meal_text)
        }
    }

}