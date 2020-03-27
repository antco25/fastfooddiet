package com.example.fastfooddiet.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.data.Category
import com.example.fastfooddiet.databinding.ListItemCategoryBinding

class CategoryListAdapter(private val onItemClick : (String) -> Unit,
                          private val onLongClick : (String) -> Unit,
                          private var dataset : List<Category>? = null,
                          private var selectedItems : Set<String>? = null) :
    RecyclerView.Adapter<CategoryListAdapter.CategoryListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        return CategoryListViewHolder(ListItemCategoryBinding
            .inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClick, onLongClick)
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        dataset?.let {dataset ->
            val result = dataset[position]

            selectedItems?.let {selectedSet ->
                holder.bind(result, selectedSet.contains(result.name()))
            } ?: run {
                holder.bind(result,false)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset?.size ?: 0
    }

    fun setData(dataset: List<Category>) {
        this.dataset = dataset
        notifyDataSetChanged()
    }

    fun setSelectedItems(selectedItems: Set<String>) {
        this.selectedItems = selectedItems
        notifyDataSetChanged()
    }

    //**** VIEW HOLDER ****
    class CategoryListViewHolder(private val binding : ListItemCategoryBinding,
                               private val onItemClick: (String) -> Unit,
                               private val onLongClick : (String) -> Unit)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                setOnClick {
                    binding.result?.let {onItemClick(it.name())}
                }

                textView.setOnLongClickListener {
                    binding.result?.let {onLongClick(it.name())}
                    true
                }
            }
        }

        fun bind(_result : Category, _isClicked : Boolean) {
            binding.apply {
                result = _result
                isClicked = _isClicked
                executePendingBindings()
            }
        }
    }
}