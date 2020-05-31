package com.example.fastfooddiet.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.fastfooddiet.R
import com.example.fastfooddiet.databinding.FragmentDetailBinding
import com.example.fastfooddiet.viewmodels.DetailViewModel
import com.example.fastfooddiet.viewmodels.SharedViewModel

class DetailFragment : Fragment() {

    //**** PROPERTIES ****
    private lateinit var detailViewModel: DetailViewModel
    private val sharedViewModel: SharedViewModel by navGraphViewModels(R.id.nav_detail)
    private val args: DetailFragmentArgs by navArgs()

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Get ViewModel
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java).apply {
            setFood(args.foodId)
            isCustomNutritionData.value = isCustomData(context)
            mealDatas.observe(viewLifecycleOwner, Observer {  })
        }

        setupSharedViewModel()

        val binding = FragmentDetailBinding.inflate(inflater, container, false)
            .apply {
                viewmodel = detailViewModel
                fragment = this@DetailFragment
                lifecycleOwner = viewLifecycleOwner
                setupToolBar(activity as AppCompatActivity, detailFragToolbar)
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

    fun setFavorite(id: Int, isFavorite: Boolean) {
        //Show toast
        val message = when (isFavorite) {
            true -> "Removed from favorites"
            false -> "Added to favorites"
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        //Update database
        detailViewModel.setFavorite(id, isFavorite)
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

    fun toMealDialog() {
        detailViewModel.mealDatas.value?.let { list ->
            val mealArray = Array<String>(list.size){ index->
                list[index].name
            }

            val action = DetailFragmentDirections
                .toMealDialog(R.id.nav_detail, mealArray)
            findNavController().navigate(action)
        }
    }

    fun setupSharedViewModel() {

        /*
        If user selects "Add New Meal" from Meal Dialog, close dialog, wait until
        the destination is back to DetailFragment, then open the TextInputDialog
         */

        findNavController().apply {
            val fragmentDestination = currentDestination
            addOnDestinationChangedListener { _, navDestination, _ ->
                if (navDestination == fragmentDestination &&
                        sharedViewModel.isAddMeal) {
                    sharedViewModel.isAddMeal = false
                    val action = DetailFragmentDirections
                        .toTextInputDetailDialog(R.id.nav_detail, "Create A New Meal")
                    findNavController().navigate(action)
                }
            }
        }

        /*
        From TextInputDialog, once the user selects a name for the meal,
        add the food to the meal then add them to the database
        */

        sharedViewModel.textInput.observe(viewLifecycleOwner, Observer { mealName ->
            //Add meal to database and food to that meal
            if (sharedViewModel.textChanged && mealName.isNotBlank()) {
                sharedViewModel.textChanged = false
                detailViewModel.addMealAndFood(mealName, args.foodId)

                //Show toast
                Toast.makeText(context, "Added to $mealName", Toast.LENGTH_SHORT).show()
            } else {
                //Show invalid toast
                Toast.makeText(context, "Invalid name", Toast.LENGTH_SHORT).show()
            }
        })

        /*
        From MealDialog, once the user selects an existing meal, add this food to it
        */

        sharedViewModel.mealData.observe(viewLifecycleOwner, Observer { mealData ->
            if (sharedViewModel.mealDataChanged) {
                sharedViewModel.mealDataChanged = false

                detailViewModel.mealDatas.value?.let {mealList->

                    //Double check that the data from the dialog and viewmodel matches

                    if (mealData.mealId < mealList.size) {
                        val mealDataFrag = mealList[mealData.mealId]

                        if (mealData.name == mealDataFrag.name) {
                            detailViewModel.addMealFood(mealDataFrag.mealId, args.foodId)

                            //Show toast
                            Toast.makeText(context, "Added to ${mealDataFrag.name}",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }

}