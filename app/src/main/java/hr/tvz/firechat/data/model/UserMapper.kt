package hr.tvz.firechat.data.model

import com.google.firebase.auth.FirebaseUser

object UserMapper {

    fun convertFirebaseUserToLocal(firebaseUser: FirebaseUser): User {
        return User(firebaseUser.uid, firebaseUser.displayName, firebaseUser.email,
            firebaseUser.photoUrl.toString())
    }
}