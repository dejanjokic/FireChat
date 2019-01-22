package hr.tvz.firechat.ui.chat

import hr.tvz.firechat.data.model.ChatMessage
import hr.tvz.firechat.ui.base.BaseContract

interface ChatContract {

    interface View : BaseContract.View {

        fun showMessages(messages: List<ChatMessage>)

        fun sendMessage()
    }

    interface Presenter : BaseContract.Presenter<View> {

        fun loadMessages()

        fun sendMessage(message: ChatMessage)
    }
}