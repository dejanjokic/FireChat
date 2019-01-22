package hr.tvz.firechat.di.component

import dagger.Component
import hr.tvz.firechat.di.module.AppModule
import hr.tvz.firechat.di.module.MainModule
import hr.tvz.firechat.ui.login.LoginActivity
import hr.tvz.firechat.ui.main.MainActivity
import hr.tvz.firechat.ui.chat.ChatFragment
import hr.tvz.firechat.ui.profile.UserProfileFragment
import hr.tvz.firechat.ui.userlist.UserListFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, MainModule::class])
interface MainComponent {

    fun inject(loginActivity: LoginActivity)

    fun inject(mainActivity: MainActivity)

    fun inject(chatFragment: ChatFragment)

    fun inject(userListFragment: UserListFragment)

    fun inject(userProfileFragment: UserProfileFragment)
}