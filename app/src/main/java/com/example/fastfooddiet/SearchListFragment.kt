package com.example.fastfooddiet

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.adapters.SearchListAdapter
import com.example.fastfooddiet.databinding.FragmentListBinding
import com.example.fastfooddiet.viewmodels.SearchListViewModel

class SearchListFragment : Fragment() {

    //**** PROPERTIES ****
    private lateinit var searchListViewModel: SearchListViewModel
    private lateinit var searchView : SearchView

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentListBinding.inflate(inflater, container, false)

        //Get ViewModel
        searchListViewModel = ViewModelProvider(this).get(SearchListViewModel::class.java)

        setupToolBar(activity as AppCompatActivity, binding.toolbar)

        setupRecyclerView(binding.searchList, searchListViewModel)

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        setupSearchView(menu)
    }

    override fun onDestroyView() {
        Log.d("Logger", "OnDestroyView - SearchList")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d("Logger", "OnDestroy - SearchList")
        super.onDestroy()
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
                                  searchListViewModel: SearchListViewModel) {
        val viewAdapter = SearchListAdapter(null)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@SearchListFragment.activity)
            adapter = viewAdapter
        }

        searchListViewModel.searchResults.observe(viewLifecycleOwner, Observer { results ->
            viewAdapter.setData(results)
        })
    }

    private fun setupSearchView(menu : Menu) {
        //TODO: Remove icon image and fix margin
        (menu.findItem(R.id.menu_search).actionView as SearchView).apply {

            setIconifiedByDefault(false)

            //Show keyboard when fragment is loaded
            setIconified(false)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean { return false }

                override fun onQueryTextChange(newQuery: String?): Boolean {
                    searchListViewModel.search(newQuery)
                    return false
                }
            })
        }.also { searchView = it }
    }



}