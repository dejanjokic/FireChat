package hr.tvz.firechat.ui.chat

import hr.tvz.firechat.data.model.ChatMessage
import hr.tvz.firechat.ui.base.BaseContract

interface ChatContract {

    interface View : BaseContract.View {

        fun showMessages(messages: List<ChatMessage>)

        fun showTextDialog()

        fun showGalleryDialog()

        fun showCameraDialog()

        fun checkCameraPermission()
    }

    interface Presenter : BaseContract.Presenter<View> {

        fun loadMessages()

        fun sendMessage(text: String? = "", imageUrl: String? = "", type: ChatMessage.Type = ChatMessage.Type.TEXT)
    }
}