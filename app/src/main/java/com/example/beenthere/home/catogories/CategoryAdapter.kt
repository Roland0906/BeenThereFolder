package com.example.beenthere.home.catogories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.beenthere.R
import com.example.beenthere.data.Experience
import com.example.beenthere.databinding.CategoryListItemBinding
import com.example.beenthere.databinding.ItemViewCategoryExpBinding

class CategoryAdapter(private val onclickListener: OnClickListener) :
    ListAdapter<Experience, CategoryAdapter.ExpViewHolder>(DiffCallback) {

    private val VIEW_TYPE_1 = 1
    private val VIEW_TYPE_2 = 2
    private val VIEW_TYPE_3 = 3

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

    override fun getItemViewType(position: Int): Int {
        // Return a view type in a cyclical pattern
        return when (position % 3) {
            0 -> VIEW_TYPE_1
            1 -> VIEW_TYPE_2
            2 -> VIEW_TYPE_3
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    class ExpViewHolder (private var binding:
//                         CategoryListItemBinding
                         ItemViewCategoryExpBinding
    ):
        RecyclerView.ViewHolder(binding.root) {
        private val fakeImages = listOf(R.drawable.avatar, R.drawable.avatar_2, R.drawable.avatar_3_female, R.drawable.avatar_4_female, R.drawable.avatar_5, R.drawable.avatar_6)
        fun bindType1(exp: Experience) {
            binding.userFrame.setBackgroundResource(R.drawable.one_corner)
            binding.avatar.setBackgroundResource(fakeImages.random())
            binding.exp = exp
            binding.executePendingBindings()
        }

        fun bindType2(exp: Experience) {
            binding.userFrame.setBackgroundResource(R.drawable.one_corner_2)
            binding.avatar.setBackgroundResource(fakeImages.random())
            binding.exp = exp
            binding.executePendingBindings()
        }

        fun bindType3(exp: Experience) {
            binding.userFrame.setBackgroundResource(R.drawable.one_corner_3)
            binding.avatar.setBackgroundResource(fakeImages.random())
            binding.exp = exp
            binding.executePendingBindings()
        }

//        fun bind(exp: Experience) {
//            binding.avatar.setBackgroundResource(fakeImages.random())
//            binding.exp = exp
//            binding.executePendingBindings()
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpViewHolder {
//        return ExpViewHolder(CategoryListItemBinding
        return ExpViewHolder(ItemViewCategoryExpBinding
            .inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ExpViewHolder, position: Int) {
        val exp = getItem(position)
//        holder.bind(exp)
//        holder.itemView.setOnClickListener {
//            onclickListener.onClick(exp)
//        }

        when (getItemViewType(position)) {
            VIEW_TYPE_1 ->  {
                holder.bindType1(exp)
                holder.itemView.setOnClickListener {
                    onclickListener.onClick(exp)
                }
            }
            VIEW_TYPE_2 -> {
                holder.bindType2(exp)
                holder.itemView.setOnClickListener {
                    onclickListener.onClick(exp)
                }
            }
            VIEW_TYPE_3 -> {
                holder.bindType3(exp)
                holder.itemView.setOnClickListener {
                    onclickListener.onClick(exp)
                }
            }
        }
    }
}