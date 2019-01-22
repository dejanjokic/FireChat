package hr.tvz.firechat.ui.login

import hr.tvz.firechat.ui.base.BaseContract

interface LoginContract {

    interface View : BaseContract.View {

        fun startLoginProcess()

        fun openMainActivity()
    }

    interface Presenter : BaseContract.Presenter<View> {

        fun performUserRouting()

        fun saveUserInfoToDatabase()
    }
}