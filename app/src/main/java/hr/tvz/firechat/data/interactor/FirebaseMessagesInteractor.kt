package hr.tvz.firechat.data.interactor

import hr.tvz.firechat.data.model.ChatMessage
import hr.tvz.firechat.data.repository.MessagesRepository
import io.reactivex.Observable
import javax.inject.Inject

class FirebaseMessagesInteractor @Inject constructor(
    private val repository: MessagesRepository
) : MessagesInteractor {

    override fun getAllMessages(): Observable<MutableList<ChatMessage>>? =
        repository.getMessagesCollection().toObservable()

    override fun sendMessage(chatMessage: ChatMessage) {
        repository.saveMessage(chatMessage)
    }
}