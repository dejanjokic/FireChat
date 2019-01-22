package hr.tvz.firechat.ui.profile

import hr.tvz.firechat.data.UserInteractor
import hr.tvz.firechat.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class UserProfilePresenter @Inject constructor(
    private val firebaseUserInteractor: UserInteractor
) : BasePresenter<UserProfileContract.View>(), UserProfileContract.Presenter {

    override fun getUserInfo(userId: String?) {
        if (userId.isNullOrEmpty()) {
            Timber.e("User ID empty!")
            view?.showError("Error: No User ID!")
        } else {
            view?.showLoading()
            Timber.w("Get user for ID: $userId")
            val d = firebaseUserInteractor.getUser(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.w("Received user $userId : $it")
                    view?.apply {
                        hideLoading()
                        showUserInfo(it)
                    }
                }, {
                    view?.showError(it.toString())
                })
            compositeDisposable.add(d)
        }
    }
}