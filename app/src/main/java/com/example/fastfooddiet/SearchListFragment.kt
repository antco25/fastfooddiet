package com.example.fastfooddiet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.adapters.SearchListAdapter
import com.example.fastfooddiet.databinding.FragmentListBinding
import com.example.fastfooddiet.viewmodels.FoodViewModel

class SearchListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var foodViewModel: FoodViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentListBinding.inflate(inflater, container, false)

        val sampleData = arrayOf("Data 1", "Data 2", "Data 3", "Data 4", "Data 5", "Data 6" )
        val viewAdapter = SearchListAdapter(sampleData)

        recyclerView = binding.searchList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@SearchListFragment.activity)
            adapter = viewAdapter
        }

        //TODO: Fix this / delete


        //Get view model
        foodViewModel = ViewModelProvider(requireActivity()).get(FoodViewModel::class.java)


        //Set observer, whenever data changes, the list will change
        foodViewModel.foods.observe(this, Observer { foods ->
            val list = ArrayList<String>()
            foods.forEach {
                list.add(it.name)
            }
            val array = arrayOfNulls<String>(list.size)
            viewAdapter.setData(list.toArray(array))
        })

        //Populate list
        foodViewModel.deleteThis()




        return binding.root
    }
}