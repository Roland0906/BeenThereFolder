package com.example.beenthere.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.beenthere.R
import com.example.beenthere.data.LiveTalkEvent
import com.example.beenthere.databinding.EventViewItemBinding


class EventAdapter(private val onclickListener: OnClickListener) :
    ListAdapter<LiveTalkEvent, EventAdapter.ExpViewHolder>(DiffCallback) {

    private val VIEW_TYPE_1 = 1
    private val VIEW_TYPE_2 = 2
    private val VIEW_TYPE_3 = 3

    class OnClickListener(val clickListener: (event: LiveTalkEvent) -> Unit) {
        fun onClick(event: LiveTalkEvent) = clickListener(event)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<LiveTalkEvent>() {
        override fun areItemsTheSame(
            oldItem: LiveTalkEvent,
            newItem: LiveTalkEvent
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: LiveTalkEvent,
            newItem: LiveTalkEvent
        ): Boolean {
            return oldItem.eventId == newItem.eventId
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
        EventViewItemBinding
    ):
        RecyclerView.ViewHolder(binding.root) {
        private val fakeImages = listOf(R.drawable.avatar, R.drawable.avatar_2, R.drawable.avatar_3_female, R.drawable.avatar_4_female, R.drawable.avatar_5, R.drawable.avatar_6)
        fun bindType1(event: LiveTalkEvent) {
//            binding.frame.setBackgroundResource(R.drawable.one_corner)
            binding.avatar.setBackgroundResource(fakeImages.random())
            binding.event = event
            binding.executePendingBindings()
        }

        fun bindType2(event: LiveTalkEvent) {
//            binding.frame.setBackgroundResource(R.drawable.one_corner_2)
            binding.avatar.setBackgroundResource(fakeImages.random())
            binding.event = event
            binding.executePendingBindings()
        }

        fun bindType3(event: LiveTalkEvent) {
//            binding.frame.setBackgroundResource(R.drawable.one_corner_3)
            binding.avatar.setBackgroundResource(fakeImages.random())
            binding.event = event
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpViewHolder {
        return ExpViewHolder(EventViewItemBinding
            .inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ExpViewHolder, position: Int) {
        val event = getItem(position)

        when (getItemViewType(position)) {
            VIEW_TYPE_1 ->  {
                holder.bindType1(event)
                holder.itemView.setOnClickListener {
                    onclickListener.onClick(event)
                }
            }
            VIEW_TYPE_2 -> {
                holder.bindType2(event)
                holder.itemView.setOnClickListener {
                    onclickListener.onClick(event)
                }
            }
            VIEW_TYPE_3 -> {
                holder.bindType3(event)
                holder.itemView.setOnClickListener {
                    onclickListener.onClick(event)
                }
            }
        }
    }
}