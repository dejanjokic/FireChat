package hr.tvz.firechat.data

import hr.tvz.firechat.data.model.User
import io.reactivex.Maybe
import io.reactivex.Observable

interface UserInteractor {

    fun getAllUsers(): Observable<MutableList<User>>?

    fun getUser(id: String): Maybe<User>
}