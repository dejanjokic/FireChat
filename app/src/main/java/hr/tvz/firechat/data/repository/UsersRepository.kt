package hr.tvz.firechat.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import hr.tvz.firechat.data.model.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

interface UsersRepository {

    fun getAllUsers(): Flowable<MutableList<User>>

    fun getUser(id: String): Maybe<User>

    fun saveUser(user: User): Completable

    fun updateUser(user: User)
}