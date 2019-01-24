package hr.tvz.firechat.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class ChatMessage(
        val id: String? = "",
        val senderId: String? = "",
        val senderName: String? = "",
        val senderAvatar: String? = "",
        val text: String? = "",
        val attachedImageUrl: String? = "",
        @ServerTimestamp val timestamp: Timestamp? = null,
        val type: Type = Type.TEXT
) {
    enum class Type {
        TEXT, IMAGE
    }
}