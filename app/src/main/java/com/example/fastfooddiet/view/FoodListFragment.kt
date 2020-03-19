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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.adapters.FoodListAdapter
import com.example.fastfooddiet.databinding.FragmentListBinding
import com.example.fastfooddiet.viewmodels.FoodListViewModel

class FoodListFragment : Fragment() {

    //**** PROPERTIES ****
    private lateinit var foodListViewModel: FoodListViewModel
    private lateinit var searchView : SearchView
    private val args : FoodListFragmentArgs by navArgs()

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Get ViewModel
        foodListViewModel = ViewModelProvider(this).get(FoodListViewModel::class.java)

        val binding = FragmentListBinding.inflate(inflater, container, false)
            .apply {
                header = args.header
                setupToolBar(activity as AppCompatActivity, listFragToolbar)
                setupRecyclerView(listFragRecyclerView, foodListViewModel)
                setupSearchView(listFragSearchView)
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

    private fun setupRecyclerView(recyclerView: RecyclerView,
                                  foodListViewModel: FoodListViewModel) {

        val viewAdapter = FoodListAdapter(null).also { foodListAdapter ->

            //Observe live data
            foodListViewModel.getFoodResults(args.showOnlyFavorite, args.searchParams)
                .observe(viewLifecycleOwner, Observer { foodListAdapter.setData(it) })

            //Show all results if set
            if (args.showAllResultsDefault) {
                foodListViewModel.showAllResultsDefault = true

                with (args.searchParams) {
                    if (this == null) {
                        foodListViewModel.search(foodListViewModel.getSearchQuery())
                    }
                    else
                        foodListViewModel.filteredSearch(foodListViewModel.filteredSearchQuery,
                            this)
                }
            }
        }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@FoodListFragment.activity)
            adapter = viewAdapter
        }
    }

    private fun setupSearchView(searchView: SearchView) {

        this.searchView = searchView.apply {

            //Show keyboard when fragment is loaded
            if (args.showKeyboardOnEnter)
               setIconified(false)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean { return false }

                override fun onQueryTextChange(newQuery: String?): Boolean {
                    with (args.searchParams) {
                        if (this == null)
                            foodListViewModel.search(newQuery)
                        else
                            foodListViewModel.filteredSearch(newQuery, this)
                    }
                    return false
                }
            })

            //Set initial searchview text
            if (args.searchParams == null) {
                setQuery(foodListViewModel.getSearchQuery(), false)
            } else
                setQuery(foodListViewModel.filteredSearchQuery, false)
        }
    }

    private fun closeKeyboard() {
        searchView.clearFocus()
    }
}