package hr.tvz.firechat.ui.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hr.tvz.firechat.App
import hr.tvz.firechat.R
import hr.tvz.firechat.data.model.ChatMessage
import hr.tvz.firechat.util.ext.gone
import hr.tvz.firechat.util.ext.visible
import kotlinx.android.synthetic.main.fragment_chat.*
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ChatFragment : Fragment(), ChatContract.View {

    @Inject lateinit var chatPresenter: ChatContract.Presenter

    private val chatAdapter = ChatAdapter {
        // Click?
    }

    companion object {
        fun newInstance() = ChatFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as App).component.inject(this)
        chatPresenter.attach(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_chat, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatPresenter.loadMessages()

        recyclerViewChatMessages.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }

        buttonSend.setOnClickListener { sendMessage() }
    }

    override fun onDestroy() {
        chatPresenter.detach()
        super.onDestroy()
    }

    override fun showLoading() {
        recyclerViewChatMessages.gone()
        progressBarChat.visible()
    }

    override fun hideLoading() {
        progressBarChat.gone()
        recyclerViewChatMessages.visible()
    }

    override fun showError(errorMessage: String) {

    }

    override fun showMessages(messages: List<ChatMessage>) {
        chatAdapter.submitList(messages.sortedWith(compareBy { it.timestamp }))
        for (m in messages) {
            Timber.w("Message: $m")
        }
    }

    override fun sendMessage() {
        val text = textInputLayout.editText?.text.toString().trim()
        if (!text.isEmpty()) {
            chatPresenter.sendMessage(ChatMessage(UUID.randomUUID().toString(), "myId", "theirId", text, LocalDateTime.now().toString()))
            textInputLayout.editText?.text = null
        }
    }
}