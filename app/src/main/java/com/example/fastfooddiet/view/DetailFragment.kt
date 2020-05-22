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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fastfooddiet.R
import com.example.fastfooddiet.databinding.FragmentDetailBinding
import com.example.fastfooddiet.viewmodels.DetailViewModel

class DetailFragment : Fragment() {

    //**** PROPERTIES ****
    private lateinit var detailViewModel: DetailViewModel
    private val args : DetailFragmentArgs by navArgs()

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
        }

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

    fun setFavorite(id: Int, isFavorite : Boolean) {
        //Show toast
        val message = when (isFavorite) {
            true -> "Removed from favorites"
            false -> "Added to favorites"
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        //Update database
        detailViewModel.setFavorite(id, isFavorite)
    }

    private fun isCustomData(context : Context?) : Boolean {
        context?.apply {
            val sharedPref = getSharedPreferences(
                resources.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE)

            val value = sharedPref.getInt(resources
                .getString(R.string.nutrition_key), 0)
            return (value == 1)
        }
        return false
    }

}