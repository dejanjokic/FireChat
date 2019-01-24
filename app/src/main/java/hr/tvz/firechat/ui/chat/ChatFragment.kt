package hr.tvz.firechat.ui.chat

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.florent37.runtimepermission.rx.RxPermissions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace.UNCOMPUTED_PROBABILITY
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions.*
import hr.tvz.firechat.App
import hr.tvz.firechat.R
import hr.tvz.firechat.data.model.ChatMessage
import hr.tvz.firechat.util.Constants.MLKit.RESULT_LOAD_IMAGE
import hr.tvz.firechat.util.ext.gone
import hr.tvz.firechat.util.ext.visible
import kotlinx.android.synthetic.main.dialog_camera_message.view.*
import kotlinx.android.synthetic.main.dialog_text_message.view.*
import kotlinx.android.synthetic.main.fragment_chat.*
import timber.log.Timber
import javax.inject.Inject

class ChatFragment : Fragment(), ChatContract.View {

    @Inject lateinit var chatPresenter: ChatContract.Presenter

    private val chatAdapter = ChatAdapter(FirebaseAuth.getInstance().currentUser!!.uid, {
        // OnClick?
    })

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

        fabText.setOnClickListener {
            showTextDialog()
            fabMenu.close(true)
        }

        fabGallery.setOnClickListener {
            showGalleryDialog()
            fabMenu.close(true)
        }

        fabCamera.setOnClickListener {
            checkCameraPermission()
            fabMenu.close(true)
        }
    }

    override fun onDestroy() {
        chatPresenter.detach()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

            if (data != null && data.data != null) {
                processFace(data.data)
            } else {
                // TODO: Error
            }
        }
    }

    override fun showLoading() {
        recyclerViewChatMessages.gone()
        progressBarChat.visible()
    }

    override fun hideLoading() {
        progressBarChat.gone()
        fabMenu.visible()
        recyclerViewChatMessages.visible()
    }

    override fun showError(errorMessage: String) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun showMessages(messages: List<ChatMessage>) {
        chatAdapter.submitList(messages.sortedWith(compareBy { it.timestamp }))
        // TODO: Fix scroll!
        recyclerViewChatMessages.smoothScrollToPosition(chatAdapter.itemCount - 1)
    }

    override fun showTextDialog() {

        val view = layoutInflater.inflate(R.layout.dialog_text_message, null)

        val dialog = AlertDialog.Builder(context!!)
            .setView(view)
            .setTitle(R.string.dialog_text_title)
            .create()

        dialog.show()

        view.fabSendMessage.setOnClickListener {

            val text = view.editTextChatMessage.text.toString().trim()
            if (!text.isEmpty()) {
                chatPresenter.sendMessage(text = text)
                dialog.dismiss()
            } else {
                // TODO: Empty message
            }
        }
    }

    override fun showGalleryDialog() {
        val i = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(i, getString(R.string.dialog_gallery_title)), RESULT_LOAD_IMAGE)
    }

    override fun showCameraDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_camera_message, null)

        val dialog = AlertDialog.Builder(context!!)
            .setView(view)
            .setTitle(R.string.dialog_camera_title)
            .create()

        dialog.show()
//        val cameraViewww = view.cameraView

        view.buttonCamera.setOnClickListener {
//            val fotoapparat = Fotoapparat(context = context!!, view = cameraViewww)
            Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("CheckResult")
    override fun checkCameraPermission() {
        RxPermissions(this).request(Manifest.permission.CAMERA).subscribe(
            { result ->
                if (result.isAccepted) {
                    showCameraDialog()
                } else {
                    showError(getString(R.string.permission_camera_denied))
                }
            },
            { t ->
                Timber.e("Error requesting permission: $t")
            }
        )
    }

    private fun processFace(uri: Uri) {
        val image = FirebaseVisionImage.fromFilePath(context!!, uri)

        val options = FirebaseVisionFaceDetectorOptions.Builder()
            .setPerformanceMode(ACCURATE)
            .setLandmarkMode(ALL_LANDMARKS)
            .setClassificationMode(ALL_CLASSIFICATIONS)
            .build()

        val detector = FirebaseVision.getInstance()
            .getVisionFaceDetector(options)

        detector.detectInImage(image).addOnSuccessListener { faces ->

            // TODO: Only one face
            for (face in faces) {
                // Classification
                if (face.smilingProbability != UNCOMPUTED_PROBABILITY) {
                    val smileProb = face.smilingProbability
                    Timber.d("Smiling probability: $smileProb")
                }

                if (face.leftEyeOpenProbability != UNCOMPUTED_PROBABILITY) {
                    val leftEyeOpenProb = face.leftEyeOpenProbability
                    Timber.d("Left eye open probability: $leftEyeOpenProb")
                }

                if (face.rightEyeOpenProbability != UNCOMPUTED_PROBABILITY) {
                    val rightEyeOpenProb = face.rightEyeOpenProbability
                    Timber.d("Right eye open probability: $rightEyeOpenProb")
                }
            }
        }
            .addOnFailureListener {
                showError(it.toString())
            }
    }
}
