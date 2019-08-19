package hr.tvz.firechat.ui.profile

import hr.tvz.firechat.data.interactor.UserInteractor
import hr.tvz.firechat.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserProfilePresenter @Inject constructor(
    private val firebaseUserInteractor: UserInteractor
) : BasePresenter<UserProfileContract.View>(), UserProfileContract.Presenter {

    override fun getUserInfo(userId: String?) {
        if (userId.isNullOrEmpty()) {
            view?.showError("Error: No User ID")
        } else {
            view?.showLoading()
            val d = firebaseUserInteractor.getUser(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
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