package hr.tvz.firechat.data.interactor

import hr.tvz.firechat.data.model.User
import hr.tvz.firechat.data.repository.UserRepository
import io.reactivex.Maybe
import io.reactivex.Observable
import javax.inject.Inject

class FirebaseUserInteractor @Inject constructor(
    private val repository: UserRepository
) : UserInteractor {

    override fun getAllUsers(): Observable<MutableList<User>>? =
        repository.getAllUsers().toObservable()

    override fun getUser(id: String): Maybe<User> =
        repository.getUser(id)
}