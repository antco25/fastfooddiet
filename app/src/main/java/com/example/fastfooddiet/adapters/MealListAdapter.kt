package com.example.fastfooddiet.adapters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.R
import com.example.fastfooddiet.data.Meal
import com.example.fastfooddiet.databinding.ListItemBinding

class MealListAdapter(private var dataset : List<Meal>?,
                      private val onClick : ((Int) -> Unit)?,
                      private val onIconClick : ((Int) -> Unit)?) :
    RecyclerView.Adapter<MealListAdapter.MealListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealListViewHolder {
        //Data binding
        return MealListViewHolder(ListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false),
            onClick, onIconClick)
    }

    private var isDeleteIcon = false

    override fun onBindViewHolder(holder: MealListViewHolder, position: Int) {
        dataset?.let {
            holder.bind(it[position], isDeleteIcon)
        }
    }

    override fun getItemCount(): Int {
        return dataset?.size ?: 0
    }

    fun setData(dataSet: List<Meal>, isDeleteIcon: Boolean) {
        this.dataset = dataSet
        this.isDeleteIcon = isDeleteIcon
        notifyDataSetChanged()
    }

    fun changeIcon(isDeleteIcon: Boolean) {
        this.isDeleteIcon = isDeleteIcon
        notifyDataSetChanged()
    }

    //**** VIEW HOLDER ****
    class MealListViewHolder(private val binding : ListItemBinding,
                             private val onClick : ((Int) -> Unit)?,
                             private val onIconClick : ((Int) -> Unit)?)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.onClick = View.OnClickListener {
                binding.item?.let {item ->
                    onClick?.invoke(item.itemId)
                }
            }

            binding.onIconClick = View.OnClickListener {
                binding.item?.let { item ->
                    onIconClick?.invoke(item.itemId)
                }
            }
        }

        fun bind(item : Meal, isDeleteIcon : Boolean) {
            binding.apply {
                this.item = item

                if (isDeleteIcon)
                    itemIcon.setImageResource(R.drawable.ic_close)
                else
                    itemIcon.setImageResource(R.drawable.ic_restaurant_menu)

                executePendingBindings()
            }
        }
    }
}