package hr.tvz.firechat.data.interactor

import hr.tvz.firechat.data.model.ChatMessage
import io.reactivex.Observable

interface MessagesInteractor {

    fun getAllMessages(): Observable<MutableList<ChatMessage>>?

    fun sendMessage(chatMessage: ChatMessage)
}