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

// Samsung: mnXKglcFwwg5EsjYOWWhjnAO5gI3
class BudgetChatAdapter(private val currentUserId: String = "hkKX8vUJXVTikG93PPmwp4Wbic73", private val clickListener: (ChatMessage) -> Unit)
    : ListAdapter<ChatMessage, BudgetChatAdapter.BudgetViewHolder>(ChatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        return BudgetViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_sent_message, parent, false))
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BudgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(message: ChatMessage) {
            itemView.textViewSentMessageContent.text = "${message.text} : ${message.timestamp}"
        }
    }

    class ChatDiffCallback : DiffUtil.ItemCallback<ChatMessage>() {

        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
            oldItem == newItem
    }
}