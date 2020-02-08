package com.example.fastfooddiet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.adapters.SearchListAdapter

class SearchListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val sampleData = arrayOf("Data 1", "Data 2", "Data 3", "Data 4", "Data 5", "Data 6" )
        viewAdapter = SearchListAdapter(sampleData)
        recyclerView = view.findViewById<RecyclerView>(R.id.search_list).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@SearchListFragment.activity)
            adapter = viewAdapter
        }
        return view
    }
}