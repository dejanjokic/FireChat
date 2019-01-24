package hr.tvz.firechat.ui.main

import hr.tvz.firechat.data.interactor.AuthUserInteractor
import hr.tvz.firechat.ui.base.BasePresenter
import javax.inject.Inject

class MainPresenter @Inject constructor(private val authUserInteractor: AuthUserInteractor)
    : BasePresenter<MainContract.View>(), MainContract.Presenter {

    override fun getCurrentUserId(): String {
        return authUserInteractor.getCurrentUser()?.id ?: ""
    }

    override fun logoutUser() {
        authUserInteractor.logoutUser()
    }
}