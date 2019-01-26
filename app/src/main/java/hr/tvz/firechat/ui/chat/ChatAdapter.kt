package hr.tvz.firechat.ui.chat

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import hr.tvz.firechat.R
import hr.tvz.firechat.data.model.ChatMessage
import hr.tvz.firechat.util.ext.gone
import hr.tvz.firechat.util.ext.visible
import hr.tvz.firechat.util.glide.GlideApp
import kotlinx.android.synthetic.main.list_item_received_message.view.*
import kotlinx.android.synthetic.main.list_item_sent_message.view.*
import timber.log.Timber

class ChatAdapter(private val currentUserId: String, private val clickListener: (ChatMessage) -> Unit)
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

    // TODO: Timestamp?, SenderName?
    abstract class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(chatMessage: ChatMessage, clickListener: (ChatMessage) -> Unit)
    }

    inner class ReceivedMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {

        override fun bind(chatMessage: ChatMessage, clickListener: (ChatMessage) -> Unit) = with(itemView) {

            // TODO: Placeholder
            GlideApp.with(itemView)
                    .load(chatMessage.senderAvatar)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView)

            if (chatMessage.type == ChatMessage.Type.EMOTION) {

                imageViewReceivedMessageEmotion.visible()
                textViewReceivedMessageContent.gone()

                GlideApp.with(itemView)
                        .load(getFaceResourceId(context, chatMessage.emotion!!))
                        .into(imageViewSentMessageEmotion)

                Timber.d("Ok")

            } else {
                imageViewReceivedMessageEmotion.gone()
                textViewReceivedMessageContent.visible()

                textViewReceivedMessageContent.text = chatMessage.text
            }

        }
    }

    inner class SentMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {

        override fun bind(chatMessage: ChatMessage, clickListener: (ChatMessage) -> Unit) = with(itemView) {

            if (chatMessage.type == ChatMessage.Type.EMOTION) {

                imageViewSentMessageEmotion.visible()
                textViewSentMessageContent.gone()

                Timber.d("Emotion: ${chatMessage.emotion}")

                GlideApp.with(itemView)
                        .load(getFaceResourceId(context, chatMessage.emotion!!))
                        .into(imageViewSentMessageEmotion)

                Timber.d("Ok")

            } else {
                imageViewSentMessageEmotion.gone()
                textViewSentMessageContent.visible()

                textViewSentMessageContent.text = chatMessage.text
            }
        }
    }

    class ChatDiffCallback : DiffUtil.ItemCallback<ChatMessage>() {

        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
                oldItem == newItem
    }

    private fun getFaceResourceId(context: Context, emotion: ChatMessage.Emotion): Drawable {
        val resourceId = context.resources.getIdentifier(emotion.name.toLowerCase(), "drawable",
                context.packageName)

        return context.resources.getDrawable(resourceId)
    }
}