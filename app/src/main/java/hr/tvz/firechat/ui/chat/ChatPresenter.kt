package hr.tvz.firechat.ui.chat

import hr.tvz.firechat.data.MessagesInteractor
import hr.tvz.firechat.data.model.ChatMessage
import hr.tvz.firechat.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ChatPresenter @Inject constructor(
    private val firebaseMessagesInteractor: MessagesInteractor
) : BasePresenter<ChatContract.View>(), ChatContract.Presenter {

    override fun loadMessages() {
        view?.showLoading()
        val d = firebaseMessagesInteractor.getAllMessages()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                view?.apply {
                    hideLoading()
                    showMessages(it)
                }
            }, {
                Timber.w("Error getting messages: $it")
            })
        if (d != null) {
            compositeDisposable.add(d)
        }
    }

    override fun sendMessage(message: ChatMessage) {
        Timber.w("Presenter sending message...")
        firebaseMessagesInteractor.sendMessage(message)
    }
}