package com.example.beenthere

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.beenthere.data.Message


const val SENT_BY_ME = 0
const val SENT_BY_OTHERS = 1

class MessageAdapter: ListAdapter<Message, MessageAdapter.MessageViewHolder>(DiffCallback) {


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
            return oldItem.message == newItem.message && oldItem.id == newItem.id && oldItem.timestamp == newItem.timestamp
        }
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageViewHolder {
        return when (viewType) {
            SENT_BY_ME -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.user_chat, parent, false)
                MessageViewHolder(view)
            }

            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.others_chat, parent, false)
                MessageViewHolder(view)
            }
        }
    }


    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message)
    }

//    override fun getItemCount(): Int = messageList.size

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        return when (message.sentBy) {
            Message.SENT_BY_ME -> SENT_BY_ME
            else -> SENT_BY_OTHERS
        }
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val leftChatTextView: TextView? = itemView.findViewById(R.id.left_chat_text_view)
        private val leftChatTimestamp: TextView? = itemView.findViewById(R.id.left_chat_timestamp)
        private val rightChatTextView: TextView? = itemView.findViewById(R.id.right_chat_text_view)
        private val rightChatTimestamp: TextView? = itemView.findViewById(R.id.right_chat_timestamp)

        fun bind(message: Message) {
            when (message.sentBy) {
                Message.SENT_BY_ME -> {
                    rightChatTextView?.text = message.message
                    rightChatTimestamp?.text = message.timestamp
                }
                else -> {
                    leftChatTextView?.text = message.message
                    leftChatTimestamp?.text = message.timestamp
                }
            }
        }
    }
}