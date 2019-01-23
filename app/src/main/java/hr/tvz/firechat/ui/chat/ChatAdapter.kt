package hr.tvz.firechat.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.firechat.R
import hr.tvz.firechat.data.model.ChatMessage
import kotlinx.android.synthetic.main.list_item_received_message.view.*
import kotlinx.android.synthetic.main.list_item_sent_message.view.*

// Samsung: mnXKglcFwwg5EsjYOWWhjnAO5gI3
class ChatAdapter(private val currentUserId: String = "hkKX8vUJXVTikG93PPmwp4Wbic73", private val clickListener: (ChatMessage) -> Unit)
  : ListAdapter<ChatMessage, ChatAdapter.MessageViewHolder>(ChatDiffCallback()) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
    return when (viewType) {
      R.layout.list_item_sent_message -> SentMessageViewHolder(LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_sent_message, parent,false))
      else -> ReceivedMessageViewHolder(LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_received_message, parent, false))
    }
  }

  override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
    holder.bind(getItem(position), clickListener)
  }

  override fun getItemViewType(position: Int): Int {
    return when (getItem(position).senderId) {
      currentUserId -> R.layout.list_item_sent_message
      else -> R.layout.list_item_received_message
    }
  }

  abstract class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(chatMessage: ChatMessage, clickListener: (ChatMessage) -> Unit)
  }

  class ReceivedMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {

    override fun bind(chatMessage: ChatMessage, clickListener: (ChatMessage) -> Unit) = with(itemView) {

      textViewReceivedMessageContent.text = chatMessage.text
    }
  }

  class SentMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {

    override fun bind(chatMessage: ChatMessage, clickListener: (ChatMessage) -> Unit) = with(itemView) {

      textViewSentMessageContent.text = chatMessage.text
    }
  }

  class ChatDiffCallback : DiffUtil.ItemCallback<ChatMessage>() {

    override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
      oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
      oldItem == newItem
  }
}