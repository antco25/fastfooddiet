package com.example.fastfooddiet.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.R
import com.example.fastfooddiet.databinding.ListItemSearchBinding

class SearchListAdapter(private var dataSet : Array<String>) :
    RecyclerView.Adapter<SearchListAdapter.SearchListViewHolder>() {

    class SearchListViewHolder(private val binding : ListItemSearchBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(result : String) {
            binding.result = result
            binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: SearchListViewHolder, position: Int) {
        holder.bind(dataSet[position])

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListViewHolder {
        return SearchListViewHolder(ListItemSearchBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun setData(dataSet: Array<String>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }
}