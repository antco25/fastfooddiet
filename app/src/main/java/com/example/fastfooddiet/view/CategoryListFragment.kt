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
import com.example.fastfooddiet.R
import com.example.fastfooddiet.adapters.StringListAdapter
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
            }

        setupToolBar(activity as AppCompatActivity, binding.listCatFragToolbar)
        setupRecyclerView(binding.listCatFragRecyclerView, categoryListViewModel)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        setupSearchView(menu)
    }

    override fun onDestroyView() {
        //Hide keyboard if open
        searchView.clearFocus()

        Log.d("Logger", "ON DESTROY VIEW")
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

        val viewAdapter = StringListAdapter(onItemClick, onLongClick)
            .also { stringListAdapter ->

                //Set live data observers
                categoryListViewModel.getCategoryResults(args.Category)
                    .observe(viewLifecycleOwner, Observer { stringListAdapter.setData(it)})
                categoryListViewModel.selectedItems
                    .observe(viewLifecycleOwner, Observer {stringListAdapter.setSelectedItems(it)})

                //Show all results by default
                categoryListViewModel.search("")
            }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@CategoryListFragment.activity)
            adapter = viewAdapter
        }
    }

    private fun setupSearchView(menu : Menu) {
        (menu.findItem(R.id.menu_search).actionView as SearchView).apply {

            setIconifiedByDefault(false)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean { return false }

                override fun onQueryTextChange(newQuery: String?): Boolean {
                    categoryListViewModel.search(newQuery)
                    return false
                }
            })
        }.also { searchView = it }
    }

    //TODO: Change header
    open fun navigateToNext() {
        val action = CategoryListFragmentDirections
            .toFoodListFragment("Browse by Category",
                false,true,
                false, getSearchParams())

        categoryListViewModel.clearSelectedItems()
        findNavController().navigate(action)
    }

    private fun getSearchParams() : SearchParams {
        val selectedList = categoryListViewModel.getSelectedItems()
        return when (args.Category) {
            Category.RESTAURANT -> SearchParams("", selectedList, null)
            Category.FOOD_TYPE -> SearchParams("", null, selectedList)
        }
    }

    enum class Category {
        RESTAURANT,
        FOOD_TYPE
    }
}