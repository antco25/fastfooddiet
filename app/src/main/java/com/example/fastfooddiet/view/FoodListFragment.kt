package com.example.fastfooddiet.view

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
import com.example.fastfooddiet.R
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
        val binding = FragmentListBinding.inflate(inflater, container, false)

        //Get ViewModel
        foodListViewModel = ViewModelProvider(this).get(FoodListViewModel::class.java)

        //Setup header
        binding.header = args.header

        setupToolBar(activity as AppCompatActivity, binding.listFragToolbar)
        setupRecyclerView(binding.listFragRecyclerView, foodListViewModel)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        setupSearchView(menu)
    }

    override fun onDestroyView() {
        //Hide keyboard if open
        searchView.clearFocus()
        super.onDestroyView()
    }

    //**** METHODS ****
    private fun setupToolBar(activity: AppCompatActivity, toolbar: Toolbar) {
        activity.setSupportActionBar(toolbar)

        //Set back button to MainFragment
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        setHasOptionsMenu(true)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView,
                                  foodListViewModel: FoodListViewModel) {

        val viewAdapter = FoodListAdapter(null).also { foodListAdapter ->

            //Set live data observer
            foodListViewModel.getFoodResults(args.showOnlyFavorite)
                .observe(viewLifecycleOwner, Observer { foodListAdapter.setData(it) })

            //Show all results if set
            if (args.showAllResultsDefault) {
                foodListViewModel.showAllResultsDefault = true
                foodListViewModel.search("")
            }
        }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@FoodListFragment.activity)
            adapter = viewAdapter
        }
    }

    //TODO: Remove icon image and fix margin
    private fun setupSearchView(menu : Menu) {
        (menu.findItem(R.id.menu_search).actionView as SearchView).apply {

            setIconifiedByDefault(false)

            //Show keyboard when fragment is loaded
            if (!args.showOnlyFavorite)
                setIconified(false)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean { return false }

                override fun onQueryTextChange(newQuery: String?): Boolean {
                    foodListViewModel.search(newQuery)
                    return false
                }
            })
        }.also { searchView = it }
    }


}