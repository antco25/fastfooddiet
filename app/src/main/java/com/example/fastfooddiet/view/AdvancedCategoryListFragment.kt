package com.example.fastfooddiet.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
import com.example.fastfooddiet.adapters.StringListAdapter
import com.example.fastfooddiet.data.SearchParams
import com.example.fastfooddiet.databinding.FragmentCatListBinding
import com.example.fastfooddiet.viewmodels.CategoryListViewModel
import com.example.fastfooddiet.viewmodels.SharedViewModel

/*
 * Used to select categories when running an advanced search
 */
class AdvancedCategoryListFragment : CategoryListFragment() {

    private val sharedViewModel : SharedViewModel by navGraphViewModels(R.id.nav_advanced_search)

    override fun navigateToNext() {
        val selectedItems = categoryListViewModel.getSelectedItems()

        when (args.Category) {
            Category.RESTAURANT -> sharedViewModel.selectedRestaurant = selectedItems
            Category.FOOD_TYPE -> sharedViewModel.selectedFoodTypes = selectedItems
        }

        categoryListViewModel.clearSelectedItems()
        findNavController().popBackStack()
    }
}