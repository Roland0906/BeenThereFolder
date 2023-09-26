package com.example.beenthere.notalone

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.example.beenthere.databinding.DialogItemViewBinding

class DialogAdapter : ListAdapter<String, DialogAdapter.SuggestionHolder>(DiffCallback) {

    class SuggestionHolder(private val binding: DialogItemViewBinding) : // bind layout
        RecyclerView.ViewHolder(binding.root) {
        fun bind(url: String) {
            binding.image = url
            binding.executePendingBindings()
        }
    }
    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SuggestionHolder {
        return SuggestionHolder(
            DialogItemViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: SuggestionHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product!!)

    }


}