package hr.tvz.firechat.ui.login

import hr.tvz.firechat.data.AuthUserInteractor
import hr.tvz.firechat.data.model.UserMapper
import hr.tvz.firechat.data.repository.UsersRepository
import hr.tvz.firechat.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class LoginPresenter @Inject constructor(
    private val firebaseAuthUserInteractor: AuthUserInteractor,
    private val usersRepository: UsersRepository
) : BasePresenter<LoginContract.View>(), LoginContract.Presenter {


    override fun saveUserInfoToDatabase() {
        val currentUser = firebaseAuthUserInteractor.getFirebaseUser()
        if (currentUser != null) {
            Timber.w("User is not null, first time?")
            if (currentUser.metadata?.creationTimestamp == currentUser.metadata?.lastSignInTimestamp) {
                Timber.w("First time user, save to db.")
                val d = usersRepository.saveUser(UserMapper.convertFirebaseUserToLocal(currentUser))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Timber.w("User successfully saved! Open MainActivity!")
                        view?.openMainActivity()
                    }, {
                        Timber.e(it)
                    })
                compositeDisposable.add(d)
            } else {
                Timber.w("Returning user, proceed without saving. Open MainActivity!")
                view?.openMainActivity()
            }
        } else {
            Timber.w("User is null, can't save!")
            // Cancel everything
        }
    }

    override fun performUserRouting() {
        val currentUser = firebaseAuthUserInteractor.getFirebaseUser()
        if (currentUser != null) {
            Timber.w("User already signed in, proceed.")
            view?.openMainActivity()
        } else {
            Timber.w("User not signed in, start loginProcess()")
            view?.startLoginProcess()
        }
    }
}