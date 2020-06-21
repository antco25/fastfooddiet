package com.example.fastfooddiet.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.example.fastfooddiet.viewcomponent.CustomSearchView
import com.example.fastfooddiet.viewmodels.FoodListViewModel

class FoodListFragment : Fragment() {

    //**** PROPERTIES ****
    private lateinit var foodListViewModel: FoodListViewModel
    private lateinit var searchView: CustomSearchView
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
                setupHeader(listFragHeader, args.header, foodListViewModel)
                setupToolBar(activity as AppCompatActivity, listFragToolbar,listFragSearchView)
                setupSearchView(listFragSearchView, args.mode, args.browseParams,
                    args.searchParams, foodListViewModel)
                setupRecyclerView(listFragRecyclerView, foodListViewModel, args.mode)
                setupEmptyResult(listFragEmpty, foodListViewModel)
                searchView = listFragSearchView
            }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //Show keyboard if it was previously open (ex orientation changes)
        foodListViewModel.showKeyboardOnStart?.let { showKeyboard ->
            if (showKeyboard) {

                //TODO: Alternative to this?
                this.view?.postDelayed({
                    searchView.requestSearchViewFocus()
                    foodListViewModel.showKeyboardOnStart = false
                }, 300)
            }

            return
        }

        //Always show keyboard when fragment is first loaded in 'Direct' mode
        if (args.mode == FoodListMode.DIRECT) {
            searchView.requestSearchViewFocus()
            foodListViewModel.showKeyboardOnStart = false
        }

    }

    override fun onStop() {
        super.onStop()

        //Save keyboard status
        foodListViewModel.showKeyboardOnStart = searchView.isSearchViewFocused()
    }

    //**** METHODS ****
    private fun setupToolBar(activity: AppCompatActivity,
                             toolbar: Toolbar,
                             searchView: CustomSearchView) {
        activity.setSupportActionBar(toolbar)

        //Set back button to MainFragment
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            if (searchView.isSearchViewFocused())
                searchView.clearSearchViewFocus()
            else
                findNavController().navigateUp()
        }

        setHasOptionsMenu(true)
    }

    private fun setupSearchView(searchView: CustomSearchView,
                                mode: FoodListMode,
                                browseParams: BrowseParams?,
                                searchParams: SearchParams?,
                                viewModel: FoodListViewModel) {

        searchView.setOnQueryTextListener(object : CustomSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearSearchViewFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                handleQueryTextChange(newText, mode, browseParams, searchParams, viewModel)
                return true
            }
        })

        //Set initial search view text
        searchView.setQuery(getSearchQuery(mode))
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        viewModel: FoodListViewModel,
        mode: FoodListMode
    ) {
        val onClick = { id: Int -> goToDetailFragment(id) }
        val onIconClick = { id: Int, _: Int, isFavorite: Boolean ->
            viewModel.setFavorite(id, isFavorite)

            val message = when (isFavorite) {
                true -> "Removed from favorites"
                false -> "Added to favorites"
            }

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        val viewAdapter = FoodListAdapter(
            null, onClick,
            onIconClick, showItemDetailWithSize(mode)
        )
            .also { adapter ->

                //Observe live data
                viewModel.getFoodResults(mode)
                    .observe(viewLifecycleOwner, Observer {
                        adapter.setData(it, null)
                        viewModel.isEmptyTextVisible(it.isEmpty(), mode)
                    })
            }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@FoodListFragment.activity)
            adapter = viewAdapter
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

    private fun showItemDetailWithSize(mode: FoodListMode): Boolean {
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
                         viewModel: FoodListViewModel
    ) {
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

    private fun setupEmptyResult(layout: GenericEmptyResultBinding,
                                 viewModel: FoodListViewModel) {
        layout.apply {
            emptyResultImage.setImageResource(R.drawable.ic_search_24dp)
            emptyResultHeader.setText(R.string.empty_food_list_header)
            emptyResultText.setText(R.string.empty_food_list_text)
        }

        viewModel.isEmptyTextVisible.observe(viewLifecycleOwner, Observer {isVisible ->
            if (isVisible)
                layout.root.visibility = View.VISIBLE
            else
                layout.root.visibility = View.INVISIBLE
        })
    }

    private fun setupHeader(header : TextView,
                            value : String,
                            viewModel: FoodListViewModel) {

        header.text = value
        viewModel.isHeaderVisible.observe(viewLifecycleOwner, Observer {isVisible ->
            if (isVisible)
                header.visibility = View.VISIBLE
            else
                header.visibility = View.INVISIBLE
        })
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

