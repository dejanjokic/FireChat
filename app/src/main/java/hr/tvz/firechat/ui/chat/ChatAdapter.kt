package hr.tvz.firechat.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.firechat.R
import hr.tvz.firechat.data.model.ChatMessage
import kotlinx.android.synthetic.main.list_item_sent_message.view.*

class ChatAdapter(private val clickListener: (ChatMessage) -> Unit) : ListAdapter<ChatMessage, ChatAdapter.ViewHolder>(ChatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_sent_message, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(chatMessage: ChatMessage, clickListener: (ChatMessage) -> Unit) = with(itemView) {
            textViewMessageContent.text = chatMessage.toString()

            setOnClickListener { clickListener(chatMessage) }
        }
    }

    class ChatDiffCallback : DiffUtil.ItemCallback<ChatMessage>() {

        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
            oldItem == newItem
    }
}