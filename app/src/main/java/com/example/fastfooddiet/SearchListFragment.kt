package com.example.fastfooddiet

import android.os.Bundle
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
import com.example.fastfooddiet.adapters.StringListAdapter
import com.example.fastfooddiet.databinding.FragmentListBinding
import com.example.fastfooddiet.viewmodels.SearchListViewModel

class SearchListFragment : Fragment() {

    //**** PROPERTIES ****
    private lateinit var searchListViewModel: SearchListViewModel
    private lateinit var searchView : SearchView
    private val args : SearchListFragmentArgs by navArgs()

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentListBinding.inflate(inflater, container, false)

        //Get ViewModel
        searchListViewModel = ViewModelProvider(this).get(SearchListViewModel::class.java)
            .apply { search("") }

        //Setup header
        binding.header = args.header

        setupToolBar(activity as AppCompatActivity, binding.searchFragToolbar)
        setupRecyclerView(binding.searchFragRecyclerView, searchListViewModel, args.searchType)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        setupSearchView(menu, args.searchType)
    }

    //**** METHODS ****
    private fun setupToolBar(activity: AppCompatActivity, toolbar: Toolbar) {
        activity.setSupportActionBar(toolbar)

        //Set back button to MainFragment
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            //Hide keyboard if open
            searchView.clearFocus()
            findNavController().navigateUp()
        }
        setHasOptionsMenu(true)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView,
                                  searchListViewModel: SearchListViewModel,
                                  searchType: SearchType) {

        /*
         * Use a StringListAdapter when SearchType is [Restaurant, Favorite]
         * Otherwise use a FoodListAdapter
         * Finally, observe the LiveData results
         */
        val viewAdapter = when (searchType) {
            SearchType.RESTAURANT, SearchType.FOOD_TYPE -> StringListAdapter(null).also {
                stringListAdapter -> searchListViewModel.stringResults.observe(viewLifecycleOwner,
                Observer { stringListAdapter.setData(it) })
            }
            else -> FoodListAdapter(null).also {
                foodListAdapter -> searchListViewModel.foodResults.observe(viewLifecycleOwner,
                Observer { foodListAdapter.setData(it) })
            }
        }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@SearchListFragment.activity)
            adapter = viewAdapter
        }
    }

    //TODO: Remove icon image and fix margin
    private fun setupSearchView(menu : Menu, searchType : SearchType) {
        (menu.findItem(R.id.menu_search).actionView as SearchView).apply {

            setIconifiedByDefault(false)

            if (args.searchType == SearchType.FOOD)
                setIconified(false) //Show keyboard when fragment is loaded

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean { return false }

                override fun onQueryTextChange(newQuery: String?): Boolean {
                    searchListViewModel.search(newQuery)
                    return false
                }
            })
        }.also { searchView = it }
    }

    enum class SearchType {
        FOOD, //Search all food items, return [Food]
        RESTAURANT, //Search all restaurants, return [String]
        FOOD_TYPE, //Search all food types, return [String]
        FAVORITE //Search all favorited food items, return [Food]
    }
}