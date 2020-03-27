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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.adapters.CategoryListAdapter
import com.example.fastfooddiet.data.SearchParams
import com.example.fastfooddiet.databinding.FragmentCatListBinding
import com.example.fastfooddiet.viewmodels.CategoryListViewModel

open class CategoryListFragment : Fragment() {

    //**** PROPERTIES ****
    protected lateinit var categoryListViewModel: CategoryListViewModel
    protected val args : CategoryListFragmentArgs by navArgs()
    private lateinit var searchView : SearchView

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Get ViewModel
        categoryListViewModel = ViewModelProvider(this).get(CategoryListViewModel::class.java)

        val binding = FragmentCatListBinding
            .inflate(inflater, container, false).apply {
                header = args.header
                viewModel = categoryListViewModel
                categoryListViewModel.setMultipleSelect(false)
                lifecycleOwner = viewLifecycleOwner
                listCatFragNextButton.setOnClickListener {navigateToNext()}
                setupToolBar(activity as AppCompatActivity, listCatFragToolbar)
                setupRecyclerView(listCatFragRecyclerView, categoryListViewModel)
                setupSearchView(listCatFragSearchView)
            }

        return binding.root
    }

    override fun onDestroyView() {
        closeKeyboard()
        Log.d("Logger", "CategoryListFragment: ON DESTROY VIEW")
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
                                  categoryListViewModel: CategoryListViewModel) {
        val onItemClick : (String) -> Unit = { item ->
            categoryListViewModel.onItemClick(item)
            if (!categoryListViewModel.checkMultipleSelect())
                navigateToNext()
        }

        val onLongClick : (String) -> Unit = { item ->
            categoryListViewModel.setMultipleSelect(true)
            categoryListViewModel.onItemClick(item)
        }

        val viewAdapter = CategoryListAdapter(onItemClick, onLongClick)
            .also { stringListAdapter ->

                //Set live data observers
                categoryListViewModel.getCategoryResults(args.CategoryType)
                    .observe(viewLifecycleOwner, Observer { stringListAdapter.setData(it)})
                categoryListViewModel.selectedItems
                    .observe(viewLifecycleOwner, Observer {stringListAdapter.setSelectedItems(it)})

                //Show all results by default
                categoryListViewModel.search(categoryListViewModel.getSearchQuery())
            }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@CategoryListFragment.activity,2)
            adapter = viewAdapter
        }
    }

    private fun setupSearchView(searchView: SearchView) {
        this.searchView = searchView.apply {

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean { return false }

                override fun onQueryTextChange(newQuery: String?): Boolean {
                    categoryListViewModel.search(newQuery)
                    return false
                }
            })

            ///Set initial searchview text
            setQuery(categoryListViewModel.getSearchQuery(), false)
        }
    }

    //TODO: Change header
    open fun navigateToNext() {
        val action = CategoryListFragmentDirections
            .toFoodListFragment("Browse by CategoryType",
                false,true,
                false, getSearchParams())

        categoryListViewModel.clearSelectedItems()
        closeKeyboard()
        findNavController().navigate(action)
    }

    private fun getSearchParams() : SearchParams {
        val selectedList = categoryListViewModel.getSelectedItems()
        return when (args.CategoryType) {
            CategoryType.RESTAURANT -> SearchParams("", selectedList, null)
            CategoryType.FOOD_TYPE -> SearchParams("", null, selectedList)
        }
    }

    open fun closeKeyboard() {
        searchView.clearFocus()
    }

    enum class CategoryType {
        RESTAURANT,
        FOOD_TYPE
    }
}