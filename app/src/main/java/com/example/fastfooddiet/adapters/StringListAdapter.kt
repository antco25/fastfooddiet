package com.example.fastfooddiet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.SearchListFragmentDirections
import com.example.fastfooddiet.data.Food
import com.example.fastfooddiet.databinding.ListItemSearchBinding
import com.example.fastfooddiet.databinding.ListItemStringBinding

class StringListAdapter(private var dataSet : List<String>?) :
    RecyclerView.Adapter<StringListAdapter.StringListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringListViewHolder {
        //Data binding
        return StringListViewHolder(ListItemStringBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: StringListViewHolder, position: Int) {
        dataSet?.let {
            holder.bind(it[position])
        }
    }

    override fun getItemCount(): Int {
        return dataSet?.size ?: 0
    }

    fun setData(dataSet: List<String>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    //**** VIEW HOLDER ****
    class StringListViewHolder(private val binding : ListItemStringBinding)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.setOnClick {
                binding.result?.let {
                        //TODO: StringList OnClick
                }
            }
        }

        fun bind(result : String) {
            binding.result = result
            binding.executePendingBindings()
        }
    }
}