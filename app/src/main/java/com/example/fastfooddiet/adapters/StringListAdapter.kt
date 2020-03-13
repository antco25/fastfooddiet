package com.example.fastfooddiet.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.data.Food
import com.example.fastfooddiet.databinding.ListItemSearchBinding
import com.example.fastfooddiet.databinding.ListItemStringBinding

class StringListAdapter(private val onItemClick : (String) -> Unit,
                        private val onLongClick : (String) -> Unit,
                        private var dataset : List<String>? = null,
                        private var selectedItems : Set<String>? = null) :
    RecyclerView.Adapter<StringListAdapter.StringListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringListViewHolder {
        return StringListViewHolder(ListItemStringBinding
            .inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClick, onLongClick)
    }

    override fun onBindViewHolder(holder: StringListViewHolder, position: Int) {
        dataset?.let {stringList->
            val result = stringList[position]

            selectedItems?.let {
                holder.bind(result, it.contains(result))
            } ?: run {
                Log.d("Logger", "Selected items is null")
                holder.bind(result, false)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset?.size ?: 0
    }

    fun setData(dataset: List<String>) {
        this.dataset = dataset
        notifyDataSetChanged()
    }

    fun setSelectedItems(selectedItems: Set<String>) {
        this.selectedItems = selectedItems
        notifyDataSetChanged()
    }

    //**** VIEW HOLDER ****
    class StringListViewHolder(private val binding : ListItemStringBinding,
                               private val onItemClick: (String) -> Unit,
                               private val onLongClick : (String) -> Unit)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.setOnClick {
                binding.result?.let {
                    onItemClick(it)
                }
            }

            binding.textView.setOnLongClickListener {
                binding.result?.let {
                    onLongClick(it)
                }
                Log.d("Logger", "Long clicked")
                true
            }
        }

        fun bind(result : String, isClicked : Boolean) {
            binding.result = result
            binding.isClicked = isClicked
            binding.executePendingBindings()
        }
    }
}