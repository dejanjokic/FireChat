package hr.tvz.firechat.ui.login

import hr.tvz.firechat.data.interactor.AuthUserInteractor
import hr.tvz.firechat.data.model.UserMapper
import hr.tvz.firechat.data.repository.UserRepository
import hr.tvz.firechat.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class LoginPresenter @Inject constructor(
    private val firebaseAuthUserInteractor: AuthUserInteractor,
    private val userRepository: UserRepository
) : BasePresenter<LoginContract.View>(), LoginContract.Presenter {


    override fun saveUserInfoToDatabase() {
        val currentUser = firebaseAuthUserInteractor.getFirebaseUser()
        if (currentUser != null) {
            if (currentUser.metadata?.creationTimestamp
                == currentUser.metadata?.lastSignInTimestamp) {
                val d = userRepository.saveUser(
                    UserMapper.convertFirebaseUserToLocal(currentUser))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        view?.openMainActivity()
                    }, {
                        Timber.e(it)
                    })
                compositeDisposable.add(d)
            } else {
                view?.openMainActivity()
            }
        } else {
            Timber.e("Error: User not found.")
        }
    }

    override fun performUserRouting() {
        val currentUser = firebaseAuthUserInteractor.getFirebaseUser()
        if (currentUser != null) {
            view?.openMainActivity()
        } else {
            view?.startLoginProcess()
        }
    }
}