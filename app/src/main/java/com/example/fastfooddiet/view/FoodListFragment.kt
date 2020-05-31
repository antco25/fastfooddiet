package com.example.fastfooddiet.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.R
import com.example.fastfooddiet.adapters.FoodListAdapter
import com.example.fastfooddiet.data.SearchParams
import com.example.fastfooddiet.databinding.FragmentFoodListBinding
import com.example.fastfooddiet.viewmodels.FoodListViewModel

class FoodListFragment : Fragment() {

    //**** PROPERTIES ****
    private lateinit var foodListViewModel: FoodListViewModel
    private lateinit var searchView: SearchView
    private val args: FoodListFragmentArgs by navArgs()

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Get ViewModel
        foodListViewModel = ViewModelProvider(this).get(FoodListViewModel::class.java)

        val binding = FragmentFoodListBinding
            .inflate(inflater, container, false)
            .apply {
                header = args.header
                viewModel = foodListViewModel
                lifecycleOwner = viewLifecycleOwner
                setupToolBar(activity as AppCompatActivity, listFragToolbar)
                setupRecyclerView(
                    listFragRecyclerView, foodListViewModel,
                    args.mode, args.searchParams
                )
                setupSearchView(listFragSearchView, args.mode, args.searchParams)
            }

        return binding.root
    }

    override fun onDestroyView() {
        closeKeyboard()
        super.onDestroyView()
    }

    //**** METHODS ****
    private fun setupToolBar(activity: AppCompatActivity, toolbar: Toolbar) {
        activity.setSupportActionBar(toolbar)

        //Set back button to MainFragment
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            closeKeyboard()
            findNavController().navigateUp()
        }

        setHasOptionsMenu(true)
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        foodListViewModel: FoodListViewModel,
        mode: FoodListMode,
        searchParams: SearchParams?
    ) {

        val onClick = { id: Int -> goToDetailFragment(id) }
        val onIconClick = { id: Int, position: Int, isFavorite : Boolean ->
            foodListViewModel.setFavorite(id, isFavorite)

            val message = when (isFavorite) {
                true -> "Removed from favorites"
                false -> "Added to favorites"
            }

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        val viewAdapter = FoodListAdapter(null, onClick, onIconClick)
            .also { adapter ->

                //Observe live data
                foodListViewModel.getFoodResults(searchParams)
                    .observe(viewLifecycleOwner, Observer {
                        adapter.setData(it, null)
                        foodListViewModel.isEmptyTextVisible(it.isEmpty())
                    })

                //Show all results if set
                if (isShowAllResultsDefault(mode)) {
                    foodListViewModel.showAllResultsDefault = true

                    searchParams?.let {
                        foodListViewModel.filteredSearch(foodListViewModel.filteredSearchQuery, it)
                    } ?: foodListViewModel.search(foodListViewModel.getSearchQuery())
                } else {
                    foodListViewModel.showAllResultsDefault = false
                }
            }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@FoodListFragment.activity)
            adapter = viewAdapter
        }
    }

    private fun setupSearchView(
        searchView: SearchView, mode: FoodListMode,
        searchParams: SearchParams?
    ) {

        this.searchView = searchView.apply {
            if (isExpandSearchView(mode)) {
                //Remove search view icon when expanded
                this.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
                    .setImageDrawable(null)

                setIconifiedByDefault(false)
                setIconified(false)
            }

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                //TODO: This is not called when query is empty. Need to clear keyboard if pressed
                override fun onQueryTextSubmit(query: String?): Boolean {
                    clearFocus()
                    return false
                }

                override fun onQueryTextChange(newQuery: String?): Boolean {

                    searchParams?.let {
                        foodListViewModel.filteredSearch(newQuery, it)
                        return false
                    }

                    foodListViewModel.search(newQuery)
                    return false
                }
            })

            //Set initial searchview text
            setQuery(getSearchQuery(mode), false)
        }
    }

    private fun closeKeyboard() {
        searchView.clearFocus()
    }

    private fun isShowAllResultsDefault(mode: FoodListMode): Boolean {
        return when (mode) {
            FoodListMode.DIRECT -> false
            FoodListMode.BROWSE -> true
        }
    }

    private fun isExpandSearchView(mode: FoodListMode): Boolean {
        return when (mode) {
            FoodListMode.DIRECT -> true
            FoodListMode.BROWSE -> false
        }
    }

    private fun getSearchQuery(mode: FoodListMode): String {
        return when (mode) {
            FoodListMode.DIRECT -> foodListViewModel.getSearchQuery()
            FoodListMode.BROWSE -> foodListViewModel.filteredSearchQuery
        }
    }

    private fun goToDetailFragment(foodId: Int) {
        val action = FoodListFragmentDirections
            .toDetailFragment(foodId)
        findNavController().navigate(action)
    }
}

enum class FoodListMode {
    DIRECT,
    BROWSE
}