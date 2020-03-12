package com.example.fastfooddiet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.data.Food
import com.example.fastfooddiet.databinding.ListItemSearchBinding
import com.example.fastfooddiet.view.FoodListFragmentDirections

class FoodListAdapter(private var dataset : List<Food>?) :
    RecyclerView.Adapter<FoodListAdapter.SearchListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListViewHolder {
        //Data binding
        return SearchListViewHolder(ListItemSearchBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SearchListViewHolder, position: Int) {
        dataset?.let {
            holder.bind(it[position])
        }
    }

    override fun getItemCount(): Int {
        return dataset?.size ?: 0
    }

    fun setData(dataSet: List<Food>) {
        this.dataset = dataSet
        notifyDataSetChanged()
    }

    //**** VIEW HOLDER ****
    class SearchListViewHolder(private val binding : ListItemSearchBinding)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.setOnClick {
                binding.result?.let {
                        food -> goToDetailFragment(food.id, it)
                }
            }
        }

        fun bind(result : Food) {
            binding.result = result
            binding.executePendingBindings()
        }

        private fun goToDetailFragment(foodId: Int, view : View) {
            val action = FoodListFragmentDirections
                .actionFoodListFragmentToDetailFragment(foodId)
            view.findNavController().navigate(action)
        }
    }
}