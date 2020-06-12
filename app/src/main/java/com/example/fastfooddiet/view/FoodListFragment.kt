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
import com.example.fastfooddiet.data.BrowseParams
import com.example.fastfooddiet.data.SearchParams
import com.example.fastfooddiet.databinding.FragmentFoodListBinding
import com.example.fastfooddiet.databinding.GenericEmptyResultBinding
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
                setupRecyclerView(listFragRecyclerView, foodListViewModel, args.mode)
                setupSearchView(listFragSearchView, args.mode, args.browseParams,
                    args.searchParams, foodListViewModel)
                setupEmptyResult(listFragEmpty)
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
        mode: FoodListMode
    ) {

        val onClick = { id: Int -> goToDetailFragment(id) }
        val onIconClick = { id: Int, _: Int, isFavorite : Boolean ->
            foodListViewModel.setFavorite(id, isFavorite)

            val message = when (isFavorite) {
                true -> "Removed from favorites"
                false -> "Added to favorites"
            }

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        val viewAdapter = FoodListAdapter(null, onClick,
            onIconClick, showItemDetailWithSize(mode))
            .also { adapter ->

                //Observe live data
                foodListViewModel.getFoodResults(mode)
                    .observe(viewLifecycleOwner, Observer {
                        adapter.setData(it, null)
                        foodListViewModel.isEmptyTextVisible(it.isEmpty(), mode)
                    })
            }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@FoodListFragment.activity)
            adapter = viewAdapter
        }
    }

    private fun setupSearchView(searchView: SearchView,
                                mode: FoodListMode,
                                browseParams: BrowseParams?,
                                searchParams: SearchParams?,
                                viewModel: FoodListViewModel) {

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
                    Log.d("xfast", "OnQueryTextChange")
                    handleQueryTextChange(newQuery, mode, browseParams, searchParams, viewModel)
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
            FoodListMode.CUSTOM -> true
        }
    }

    private fun isExpandSearchView(mode: FoodListMode): Boolean {
        return when (mode) {
            FoodListMode.DIRECT -> true
            FoodListMode.BROWSE -> false
            FoodListMode.CUSTOM -> false
        }
    }

    private fun getSearchQuery(mode: FoodListMode): String {
        return when (mode) {
            FoodListMode.DIRECT -> foodListViewModel.directSearchQuery
            FoodListMode.BROWSE -> foodListViewModel.browseSearchQuery
            FoodListMode.CUSTOM -> foodListViewModel.filteredSearchQuery
        }
    }

    private fun showItemDetailWithSize(mode : FoodListMode) : Boolean {
        return when (mode) {
            FoodListMode.DIRECT -> false
            FoodListMode.BROWSE -> false
            FoodListMode.CUSTOM -> true
        }
    }

    private fun handleQueryTextChange(newQuery: String?,
                                      mode: FoodListMode,
                                      browseParams: BrowseParams?,
                                      searchParams: SearchParams?,
                                      viewModel: FoodListViewModel) {
        newQuery?.let { query ->
            when (mode) {
                FoodListMode.DIRECT -> viewModel.directSearch(query)
                FoodListMode.BROWSE -> browseParams?.let { params ->
                    viewModel.browseSearch(query, params)
                }
                FoodListMode.CUSTOM -> searchParams?.let { params ->
                    viewModel.filteredSearch(query, params)
                }
            }
        }
    }

    private fun goToDetailFragment(foodId: Int) {
        val action = FoodListFragmentDirections
            .toDetailFragment(foodId)
        findNavController().navigate(action)
    }

    private fun setupEmptyResult(layout: GenericEmptyResultBinding) {
        layout.apply {
            emptyResultImage.setImageResource(R.drawable.ic_search_24dp)
            emptyResultHeader.setText(R.string.empty_food_list_header)
            emptyResultText.setText(R.string.empty_food_list_text)
        }
    }
}

enum class FoodListMode {
    DIRECT,
    BROWSE,
    CUSTOM
}

/*
    Food List Mode Reference

    Search Query?
    Direct - Search database where name = query
    Browse - Search database based on restaurant, foodType, name
    Custom - Search database based on multiple criteria

    Show multiple sizes of same item?
    Direct - No
    Browse - No
    Custom - Yes

    Show all results on empty query?
    Direct - No
    Browse - Yes
    Custom - Yes

    Searchview Icon status?
    Direct - Expanded
    Browse - Minimized
    Custom - Minimized

 */