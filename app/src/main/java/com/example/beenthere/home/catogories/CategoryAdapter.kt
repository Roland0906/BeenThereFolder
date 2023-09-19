package com.example.beenthere.home.catogories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.beenthere.data.Experience
import com.example.beenthere.databinding.ItemViewCategoryExpBinding

class CategoryAdapter(private val onclickListener: OnClickListener) :
    ListAdapter<Experience, CategoryAdapter.ExpViewHolder>(DiffCallback) {

    class OnClickListener(val clickListener: (exp: Experience) -> Unit) {
        fun onClick(exp: Experience) = clickListener(exp)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Experience>() {
        override fun areItemsTheSame(
            oldItem: Experience,
            newItem: Experience
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: Experience,
            newItem: Experience
        ): Boolean {
            return oldItem.situation == newItem.situation
        }
    }

    class ExpViewHolder (private var binding: ItemViewCategoryExpBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(exp: Experience) {
            binding.exp = exp
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpViewHolder {
        return ExpViewHolder(ItemViewCategoryExpBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ExpViewHolder, position: Int) {
        val exp = getItem(position)
        holder.bind(exp)
        holder.itemView.setOnClickListener {
            onclickListener.onClick(exp)
        }
    }
}