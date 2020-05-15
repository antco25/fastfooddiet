package com.example.fastfooddiet.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.data.Category
import com.example.fastfooddiet.databinding.ListItemCategoryBinding

class CategoryAdapter(private val onItemClick : (String) -> Unit,
                      private var dataSet : List<Category>? = null) :
    RecyclerView.Adapter<CategoryAdapter.CategoryListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        return CategoryListViewHolder(ListItemCategoryBinding
            .inflate(LayoutInflater.from(parent.context), parent, false), onItemClick)
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        dataSet?.let {dataSet ->
            val data = dataSet[position]
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int {
        return dataSet?.size ?: 0
    }

    fun setData(dataSet: List<Category>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    //**** VIEW HOLDER ****
    class CategoryListViewHolder(private val binding : ListItemCategoryBinding,
                                 private val onItemClick: (String) -> Unit)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                setOnClick {
                    data?.let { onItemClick(it.name()) }
                }
            }
        }
        fun bind(data : Category) {
            binding.apply {
                this.data = data
                executePendingBindings()
            }
        }
    }
}