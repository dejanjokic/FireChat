package hr.tvz.firechat.ui.chat

import android.content.Context
import android.net.Uri
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import hr.tvz.firechat.data.interactor.AuthUserInteractor
import hr.tvz.firechat.data.interactor.MessagesInteractor
import hr.tvz.firechat.data.model.ChatMessage
import hr.tvz.firechat.data.model.EmotionMapper
import hr.tvz.firechat.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ChatPresenter @Inject constructor(
        private val messagesInteractor: MessagesInteractor,
        private val authUserInteractor: AuthUserInteractor,
        private val context: Context
) : BasePresenter<ChatContract.View>(), ChatContract.Presenter {

    override fun loadMessages() {
        view?.showLoading()
        val d = messagesInteractor.getAllMessages()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    view?.apply {
                        hideLoading()
                        showMessages(it)
                    }
                }, {
                    Timber.e("Error getting messages: $it")
                    view?.apply {
                        hideLoading()
                        showError(it.toString())
                    }
                })
        if (d != null) {
            compositeDisposable.add(d)
        }
    }

    override fun sendTextMessage(text: String?) {
        val user = authUserInteractor.getCurrentUser()

        if (user != null) {
            val message = ChatMessage(
                    id = UUID.randomUUID().toString(),
                    senderId = user.id,
                    senderName = user.displayName,
                    senderAvatar = user.profilePicturePath,
                    text = text,
                    timestamp = LocalDateTime.now().toString()
            )
            messagesInteractor.sendMessage(message)
        }
    }

    override fun processAndSendEmotion(uri: Uri) {

        val image = FirebaseVisionImage.fromFilePath(context, uri)

        val options = FirebaseVisionFaceDetectorOptions.Builder()
                .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .build()

        val detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(options)

        detector.detectInImage(image).addOnFailureListener { Timber.e(it) }
                .addOnSuccessListener { faces ->

                    val face = faces.first()

                    var smilingProb = 0F
                    var leftEyeOpenProb = 0F
                    var rightEyeOpenProb = 0F

                    // Classification
                    if (face.smilingProbability !=
                        FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                        smilingProb = face.smilingProbability
                        Timber.d("Smiling probability: $smilingProb")
                    }

                    if (face.leftEyeOpenProbability !=
                        FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                        leftEyeOpenProb = face.leftEyeOpenProbability
                        Timber.d("Left eye open probability: $leftEyeOpenProb")
                    }

                    if (face.rightEyeOpenProbability !=
                        FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                        rightEyeOpenProb = face.rightEyeOpenProbability
                        Timber.d("Right eye open probability: $rightEyeOpenProb")
                    }

                    sendEmotionMessage(EmotionMapper.getEmotion(
                        smilingProb, leftEyeOpenProb, rightEyeOpenProb
                    ))
                }
    }

    override fun sendEmotionMessage(emotion: ChatMessage.Emotion) {
        val user = authUserInteractor.getCurrentUser()

        if (user != null) {
            val message = ChatMessage(
                    id = UUID.randomUUID().toString(),
                    senderId = user.id,
                    senderName = user.displayName,
                    senderAvatar = user.profilePicturePath,
                    emotion = emotion,
                    type = ChatMessage.Type.EMOTION,
                    timestamp = LocalDateTime.now().toString()
            )
            messagesInteractor.sendMessage(message)
        }
    }
}