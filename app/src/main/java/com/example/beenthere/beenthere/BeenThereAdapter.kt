package com.example.beenthere.beenthere



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.beenthere.R
import com.example.beenthere.data.Situation
import com.example.beenthere.databinding.SituationItemBinding

class BeenThereAdapter(private val onclickListener: OnClickListener) :
    ListAdapter<Situation, BeenThereAdapter.SituationViewHolder>(DiffCallback) {

    class OnClickListener(val clickListener: (situation: Situation) -> Unit) {
        fun onClick(situation: Situation) = clickListener(situation)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Situation>() {
        override fun areItemsTheSame(
            oldItem: Situation,
            newItem: Situation
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: Situation,
            newItem: Situation
        ): Boolean {
            return oldItem.userId == newItem.userId && oldItem.description == newItem.description
        }
    }

    class SituationViewHolder (private var binding: SituationItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        private val fakeImages = listOf(R.drawable.avatar, R.drawable.avatar_2, R.drawable.avatar_3_female, R.drawable.avatar_4_female, R.drawable.avatar_5, R.drawable.avatar_6)
        fun bind(situation: Situation) {
            binding.pic.setBackgroundResource(fakeImages.random())
            binding.situation = situation
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SituationViewHolder {
        return SituationViewHolder(SituationItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: SituationViewHolder, position: Int) {
        val situation = getItem(position)
        holder.bind(situation)
        holder.itemView.setOnClickListener {
            onclickListener.onClick(situation)
        }
    }
}