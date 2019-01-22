package hr.tvz.firechat.ui.userlist

import hr.tvz.firechat.data.model.User
import hr.tvz.firechat.ui.base.BaseContract

interface UserListContract {

    interface View : BaseContract.View {

        fun showUsers(users: List<User>?)

        fun showUserProfile(userId: String?)
    }

    interface Presenter : BaseContract.Presenter<View> {

        fun loadUsers()
    }
}