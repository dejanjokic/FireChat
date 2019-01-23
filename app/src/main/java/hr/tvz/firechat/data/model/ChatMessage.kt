package hr.tvz.firechat.data.model

data class ChatMessage(
    val id: String? = "",
    val senderId: String? = "",
    val senderName: String? = "",
    val senderAvatar: String? = "",
    val text: String? = "",
    val attachedImageUrl: String? = "",
    val timestamp: String? = "",
    val type: Type = Type.TEXT
) {
    enum class Type {
        TEXT, IMAGE
    }
}