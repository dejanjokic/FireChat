package hr.tvz.firechat.ui.chat

import hr.tvz.firechat.data.AuthUserInteractor
import hr.tvz.firechat.data.MessagesInteractor
import hr.tvz.firechat.data.model.ChatMessage
import hr.tvz.firechat.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ChatPresenter @Inject constructor(
    private val firebaseMessagesInteractor: MessagesInteractor,
    private val firebaseAuthUserInteractor: AuthUserInteractor
) : BasePresenter<ChatContract.View>(), ChatContract.Presenter {

    override fun loadMessages() {
        view?.showLoading()
        val d = firebaseMessagesInteractor.getAllMessages()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                view?.apply {
                    hideLoading()
                    showMessages(it)
                    scrollToLastMessage()
                }
            }, {
                Timber.w("Error getting messages: $it")
            })
        if (d != null) {
            compositeDisposable.add(d)
        }
    }

    override fun sendMessage(text: String?, imageUrl: String?, type: ChatMessage.Type) {
        val user = firebaseAuthUserInteractor.getCurrentUser()

        if (user != null) {
            val message = ChatMessage(
                id = UUID.randomUUID().toString(),
                senderId = user.id,
                senderName = user.displayName,
                senderAvatar = user.profilePicturePath,
                text = text,
                attachedImageUrl = "", // TODO
                timestamp = LocalDateTime.now().toString(),
                type = type
            )
            firebaseMessagesInteractor.sendMessage(message)
        }
    }
}