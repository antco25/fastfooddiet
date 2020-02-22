package com.example.fastfooddiet.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.R
import com.example.fastfooddiet.data.Food
import com.example.fastfooddiet.databinding.ListItemSearchBinding

class SearchListAdapter(private var dataSet : List<Food>?) :
    RecyclerView.Adapter<SearchListAdapter.SearchListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListViewHolder {
        //Data binding
        return SearchListViewHolder(ListItemSearchBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SearchListViewHolder, position: Int) {
        dataSet?.let {
            holder.bind(it[position])
        }
    }

    override fun getItemCount(): Int {
        return dataSet?.size ?: 0
    }

    fun setData(dataSet: List<Food>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    //**** VIEW HOLDER ****
    class SearchListViewHolder(private val binding : ListItemSearchBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(result : Food) {
            binding.result = result.name
            binding.executePendingBindings()
        }
    }
}