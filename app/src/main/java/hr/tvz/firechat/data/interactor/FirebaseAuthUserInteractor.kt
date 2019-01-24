package hr.tvz.firechat.data.interactor

import android.content.Context
import android.content.Intent
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import hr.tvz.firechat.data.model.User
import hr.tvz.firechat.data.model.UserMapper
import hr.tvz.firechat.ui.login.LoginActivity
import javax.inject.Inject

class FirebaseAuthUserInteractor @Inject constructor(
    private val context: Context,
    private val authUI: AuthUI,
    private val firebaseAuth: FirebaseAuth
) : AuthUserInteractor {

    // TODO: Remove
    override fun getCurrentUser(): User? =
        UserMapper.convertFirebaseUserToLocal(firebaseAuth.currentUser!!)

    override fun getFirebaseUser(): FirebaseUser? = firebaseAuth.currentUser

    override fun logoutUser() {
        authUI.signOut(context).addOnSuccessListener {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    override fun updateUser() {

    }
}