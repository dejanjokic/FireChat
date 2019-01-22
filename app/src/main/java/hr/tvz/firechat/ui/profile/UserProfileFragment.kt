package hr.tvz.firechat.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import hr.tvz.firechat.App
import hr.tvz.firechat.R
import hr.tvz.firechat.data.model.User
import hr.tvz.firechat.util.ext.gone
import hr.tvz.firechat.util.ext.visible
import kotlinx.android.synthetic.main.fragment_user_profile.*
import timber.log.Timber
import javax.inject.Inject

class UserProfileFragment : Fragment(), UserProfileContract.View {

    @Inject lateinit var userProfilePresenter: UserProfileContract.Presenter

    companion object {
        const val USER_ID = "user_id"

        fun newInstance(userId: String?) = UserProfileFragment().apply {
            arguments = Bundle().apply {
                putString(USER_ID, userId)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as App).component.inject(this)
        userProfilePresenter.attach(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_user_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userProfilePresenter.getUserInfo(arguments?.getString(USER_ID))
    }

    override fun onDestroy() {
        userProfilePresenter.detach()
        super.onDestroy()
    }

    override fun showLoading() {
        contentUserProfile.gone()
        progressBarUserProfile.visible()
    }

    override fun hideLoading() {
        progressBarUserProfile.gone()
        contentUserProfile.visible()
    }

    override fun showError(errorMessage: String) {
        Timber.w("ERRORRRRRRRRRRRRRRRRR $errorMessage")
    }

    // TODO: Icons? Editing?
    override fun showUserInfo(user: User?) = with(user) {
        if (user != null) {
            textViewUserProfileDisplayName.text = user.displayName
            textViewUserProfileEmail.text = user.email
            Glide.with(context!!)
                .load(this!!.profilePicturePath)
                .into(imageViewUserProfileAvatar)
        }
        Timber.w("***************")
        Timber.w("Display name: ${user?.displayName}")
        Timber.w("Email: ${user?.email}")
        Timber.w("***************")
    }
}