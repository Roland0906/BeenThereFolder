package com.example.beenthere.home.catogories.detail


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.beenthere.R
import com.example.beenthere.data.Message
import com.example.beenthere.databinding.ForumItemViewBinding

class ForumAdapter : ListAdapter<Message, ForumAdapter.CommentViewHolder>(DiffCallback) {

    private val VIEW_TYPE_1 = 1
    private val VIEW_TYPE_2 = 2

    companion object DiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(
            oldItem: Message,
            newItem: Message
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: Message,
            newItem: Message
        ): Boolean {
            return oldItem.id == newItem.id && oldItem.timestamp == newItem.timestamp
        }
    }

    override fun getItemViewType(position: Int): Int {

        return when (position % 2) {
            0 -> VIEW_TYPE_1
            1 -> VIEW_TYPE_2
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    class CommentViewHolder(private var binding: ForumItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val fakeImages = listOf(
            R.drawable.avatar,
            R.drawable.avatar_2,
            R.drawable.avatar_3_female,
            R.drawable.avatar_4_female,
            R.drawable.avatar_5,
            R.drawable.avatar_6
        )

        fun bind1(message: Message) {
            Log.i("ForumAdapter", "bind1")
            binding.textFrame.setBackgroundResource(R.drawable.forum_circle)
            binding.avatar.setBackgroundResource(fakeImages.random())
            binding.message = message
            binding.executePendingBindings()
        }

        fun bind2(message: Message) {
            Log.i("ForumAdapter", "bind2")
            binding.textFrame.setBackgroundResource(R.drawable.forum_circle_2)
            binding.avatar.setBackgroundResource(fakeImages.random())
            binding.message = message
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        Log.i("ForumAdapter", "onCreateViewHolder")
        return CommentViewHolder(ForumItemViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val message = getItem(position)
        Log.i("ForumAdapter", message.message)

        when (getItemViewType(position)) {
            VIEW_TYPE_1 -> {
                holder.bind1(message)
                Log.i("ForumAdapter", "bind message $message")
            }

            VIEW_TYPE_2 -> {
                holder.bind2(message)
            }

        }
    }
}