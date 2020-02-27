package com.example.fastfooddiet

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.fastfooddiet.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater, container, false)
        setupSearchView(binding.searchView)

        return binding.root
    }

    //**** METHODS ****
    private fun setupSearchView(searchView : SearchView) {
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        }
    }
}