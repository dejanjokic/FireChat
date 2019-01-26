package hr.tvz.firechat.ui.userlist

import hr.tvz.firechat.data.interactor.AuthUserInteractor
import hr.tvz.firechat.data.interactor.UserInteractor
import hr.tvz.firechat.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class UserListPresenter @Inject constructor(
    private val userInteractor: UserInteractor,
    private val authUserInteractor: AuthUserInteractor
) : BasePresenter<UserListContract.View>(), UserListContract.Presenter {

    override fun loadUsers() {
        view?.showLoading()
        val d = userInteractor.getAllUsers()?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    Timber.w("The presenter has ${it.size} users!")
                    view?.apply {
                        hideLoading()
                        showUsers(it)
                    }
                },
                {
                    view?.showError(it.message)
                }
            )
        if (d != null) {
            compositeDisposable.add(d)
        }
    }

    override fun getCurrentUserId() = authUserInteractor.getCurrentUserId() ?: ""
}