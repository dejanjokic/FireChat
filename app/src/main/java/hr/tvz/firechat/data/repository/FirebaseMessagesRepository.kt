package hr.tvz.firechat.data.repository

import android.annotation.SuppressLint
import com.google.firebase.firestore.CollectionReference
import durdinapps.rxfirebase2.RxFirestore
import hr.tvz.firechat.data.model.ChatMessage
import hr.tvz.firechat.util.Constants.Firestore.MESSAGES_COLLECTION
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class FirebaseMessagesRepository @Inject constructor(
    @Named(MESSAGES_COLLECTION) private val ref: CollectionReference
) : MessagesRepository {

    override fun getMessagesCollection(): Flowable<MutableList<ChatMessage>> =
            RxFirestore.observeQueryRef(ref, ChatMessage::class.java)

    // TODO: Completable?
    @SuppressLint("CheckResult")
    override fun saveMessage(chatMessage: ChatMessage) {
        RxFirestore.addDocument(ref, chatMessage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.w("Message successfully saved to: ${it.path}")
            }, {
                // Error saving message
                Timber.e("Error saving message: $it")
            })
    }
}