package hr.tvz.firechat.ui.profile

import hr.tvz.firechat.data.model.User
import hr.tvz.firechat.ui.base.BaseContract

interface UserProfileContract {

    interface View : BaseContract.View {

        fun showUserInfo(user: User?)
    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getUserInfo(userId: String?)
    }
}