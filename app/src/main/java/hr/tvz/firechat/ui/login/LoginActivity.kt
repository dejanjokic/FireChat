package hr.tvz.firechat.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import hr.tvz.firechat.App
import hr.tvz.firechat.BuildConfig
import hr.tvz.firechat.R
import hr.tvz.firechat.ui.main.MainActivity
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), LoginContract.View {

    @Inject lateinit var loginPresenter: LoginContract.Presenter

    companion object {
        const val RC_SIGN_IN = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).component.inject(this)
        loginPresenter.attach(this)
        loginPresenter.performUserRouting()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            val response = IdpResponse.fromResultIntent(data)

            // User successfully signed in, continue the process
            if (resultCode == Activity.RESULT_OK) {
                loginPresenter.saveUserInfoToDatabase()
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Timber.d("No sign in response")
                    return
                }
                if (response.error?.errorCode == ErrorCodes.NO_NETWORK) {
                    showError("No network connection, please try again.")
                    return
                }
                showError("Unknown error")
            }
        }
    }

    override fun startLoginProcess() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(!BuildConfig.DEBUG, true)
                .setLogo(R.mipmap.ic_firebase)
                .setAvailableProviders(
                    Arrays.asList(
                        AuthUI.IdpConfig.GoogleBuilder().build(),
                        AuthUI.IdpConfig.FacebookBuilder().build(),
                        AuthUI.IdpConfig.TwitterBuilder().build(),
                        AuthUI.IdpConfig.EmailBuilder().build(),
                        AuthUI.IdpConfig.PhoneBuilder().build()
                    ))
                .build(),
            RC_SIGN_IN
        )
    }

    override fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        finish()
    }

    override fun showError(errorMessage: String?) {
        Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
    }
}