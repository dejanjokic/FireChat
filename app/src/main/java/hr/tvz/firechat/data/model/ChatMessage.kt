package hr.tvz.firechat.data.model

// TODO: AttachedImageUrl?
data class ChatMessage(
    val id: String? = "",
    val senderId: String? = "",
    val receiverId: String? = "",
    val text: String? = "",
    val timestamp: String? = ""
)