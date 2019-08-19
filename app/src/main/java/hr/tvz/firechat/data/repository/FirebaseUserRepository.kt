package hr.tvz.firechat.data.repository

import com.google.firebase.firestore.CollectionReference
import durdinapps.rxfirebase2.RxFirestore
import hr.tvz.firechat.data.model.User
import hr.tvz.firechat.util.Constants.Firestore.USERS_COLLECTION
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Named

class FirebaseUserRepository @Inject constructor(
    @Named(USERS_COLLECTION) private val ref: CollectionReference
) : UserRepository {
    override fun getUser(id: String): Maybe<User> =
        RxFirestore.getDocument(ref.document(id), User::class.java)

    override fun getAllUsers(): Flowable<MutableList<User>> =
        RxFirestore.observeQueryRef(ref, User::class.java)

    override fun saveUser(user: User): Completable =
        RxFirestore.setDocument(ref.document(user.id), user)
}