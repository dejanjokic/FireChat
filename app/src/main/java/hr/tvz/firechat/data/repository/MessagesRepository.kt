package hr.tvz.firechat.data.repository

import com.google.firebase.firestore.QuerySnapshot
import hr.tvz.firechat.data.model.ChatMessage
import io.reactivex.Flowable

interface MessagesRepository {

    fun getMessagesCollection(): Flowable<MutableList<ChatMessage>>

    fun saveMessage(chatMessage: ChatMessage)
}