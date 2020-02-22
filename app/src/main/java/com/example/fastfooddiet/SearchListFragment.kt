package com.example.fastfooddiet

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.adapters.SearchListAdapter
import com.example.fastfooddiet.databinding.FragmentListBinding
import com.example.fastfooddiet.viewmodels.FoodViewModel
import com.example.fastfooddiet.viewmodels.SearchListViewModel

class SearchListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchListViewModel: SearchListViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentListBinding.inflate(inflater, container, false)

        //Get ViewModel
        searchListViewModel = ViewModelProvider(this).get(SearchListViewModel::class.java)

        //Setup toolbar
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        //Enable Up button TODO: Bring this back to homepage
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setHasOptionsMenu(true)

        //Setup recyclerview
        val viewAdapter = SearchListAdapter(null)
        recyclerView = binding.searchList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@SearchListFragment.activity)
            adapter = viewAdapter
        }

        //Set view model observer
        searchListViewModel.searchResults.observe(viewLifecycleOwner, Observer { results ->
            viewAdapter.setData(results)
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        setupSearchView(menu)
    }

    private fun setupSearchView(menu : Menu) {
        //TODO: Remove icon image and fix margin
        (menu.findItem(R.id.menu_search).actionView as SearchView).apply {

            setIconifiedByDefault(false)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean { return false }

                override fun onQueryTextChange(newQuery: String?): Boolean {
                    searchListViewModel.search(newQuery)
                    return false
                }
            })
        }
    }



}