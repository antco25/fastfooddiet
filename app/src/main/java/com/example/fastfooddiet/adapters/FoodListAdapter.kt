package com.example.fastfooddiet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfooddiet.R
import com.example.fastfooddiet.data.Food
import com.example.fastfooddiet.databinding.ListItemBinding

class FoodListAdapter(private var dataset : List<Food>?,
                      private val onClick : ((Int) -> Unit)?,
                      private val onIconClick : ((Int, Int, Boolean) -> Unit)?,
                      private val showItemDetailWithSize : Boolean):
    RecyclerView.Adapter<FoodListAdapter.FoodListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListViewHolder {
        return FoodListViewHolder(ListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false),
            onClick, onIconClick, showItemDetailWithSize)
    }

    private var isDeleteIcon : Boolean? = null

    override fun onBindViewHolder(holder: FoodListViewHolder, position: Int) {
        dataset?.let {items ->
            holder.bind(items[position], isDeleteIcon)
        }
    }

    override fun getItemCount(): Int {
        return dataset?.size ?: 0
    }

    fun setData(dataSet: List<Food>, isDeleteIcon: Boolean?) {
        this.dataset = dataSet
        this.isDeleteIcon = isDeleteIcon
        notifyDataSetChanged()
    }

    fun changeIcon(isDeleteIcon: Boolean) {
        this.isDeleteIcon = isDeleteIcon
        notifyDataSetChanged()
    }

    //**** VIEW HOLDER ****
    class FoodListViewHolder(private val binding : ListItemBinding,
                             private val _onClick : ((Int) -> Unit)?,
                             private val _onIconClick: ((Int, Int, Boolean) -> Unit)?,
                             private val _showItemDetailWithSize : Boolean)
        : RecyclerView.ViewHolder(binding.root) {

        private var isFavorite = false

        init {
            binding.apply {

                onClick = View.OnClickListener {
                    item?.let {item ->
                        _onClick?.invoke(item.itemId)
                    }
                }

                onIconClick = View.OnClickListener {
                    item?.let { item ->
                        _onIconClick?.invoke(item.itemId,
                            this@FoodListViewHolder.adapterPosition, isFavorite)
                    }
                }

                showItemDetailWithSize = _showItemDetailWithSize
                showItemImage = true
            }

        }

        fun bind(item : Food, _isDeleteIcon : Boolean?) {
            val isDeleteIcon = _isDeleteIcon?: false

            binding.apply {
                this.item = item

                if (isDeleteIcon)
                    itemIcon.setImageResource(R.drawable.ic_close)
                else if (item.favorite) {
                    itemIcon.setImageResource(R.drawable.ic_star)
                    isFavorite = true
                } else {
                    itemIcon.setImageResource(R.drawable.ic_star_border)
                    isFavorite = false
                }

                executePendingBindings()
            }
        }
    }
}