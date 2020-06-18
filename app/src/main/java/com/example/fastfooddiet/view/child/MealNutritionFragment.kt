package com.example.fastfooddiet.view.child

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fastfooddiet.R
import com.example.fastfooddiet.databinding.FragmentMealNutritionBinding
import com.example.fastfooddiet.viewmodels.MealViewModel

/*
 Child fragment for MealFragment's ViewPager
 */

class MealNutritionFragment : Fragment() {
    companion object {
        private val KEY_ID = "mealId"

        fun newInstance(mealId: Int): MealNutritionFragment {
            val args = Bundle()
            args.putInt(KEY_ID, mealId)

            val fragment = MealNutritionFragment()
            fragment.arguments = args
            return fragment
        }
    }

    //**** PROPERTIES ****
    private lateinit var mealViewModel: MealViewModel

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
            isCustomNutritionData.value = isCustomData(context)
        }

        val binding = FragmentMealNutritionBinding
            .inflate(inflater, container, false).apply {
                viewmodel = mealViewModel
                lifecycleOwner = viewLifecycleOwner
                setupEmptyResult(mealNutritionLayout, mealViewModel)
            }

        return binding.root
    }

    //**** METHODS ****
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

    private fun setupEmptyResult(layout: View, viewModel: MealViewModel) {
        viewModel.mealFoodCount.observe(viewLifecycleOwner, Observer { count ->
            if (count > 0) {
                layout.visibility = View.VISIBLE
            } else {
                layout.visibility = View.INVISIBLE
            }
        })
    }

}
