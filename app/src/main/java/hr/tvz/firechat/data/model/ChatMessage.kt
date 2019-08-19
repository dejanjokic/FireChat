package hr.tvz.firechat.data.model

data class ChatMessage(
        val id: String? = "",
        val senderId: String? = "",
        val senderName: String? = "",
        val senderAvatar: String? = "",
        val text: String? = "",
        val timestamp: String? = "",
        val type: Type = Type.TEXT,
        val emotion: Emotion? = Emotion.FACE0
) {
    enum class Type { TEXT, EMOTION }

    enum class Emotion { FACE0, FACE1, FACE2, FACE3, FACE4, FACE5, FACE6, FACE7, FACE8 }
}