package hr.tvz.firechat.ui.main

import hr.tvz.firechat.ui.base.BaseContract

interface MainContract {

    interface View : BaseContract.View {

        fun showMessageView()

        fun showUserListView()

        fun showProfileView()

        fun onLogoutClick()
    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getCurrentUserId(): String

        fun logoutUser()
    }
}