package hr.tvz.firechat.data.interactor

import com.google.firebase.auth.FirebaseUser
import hr.tvz.firechat.data.model.User

interface AuthUserInteractor {

    fun getFirebaseUser(): FirebaseUser?

    fun logoutUser()

    fun getCurrentUser(): User?

    fun getCurrentUserId(): String?
}