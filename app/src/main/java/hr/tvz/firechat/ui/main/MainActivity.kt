package hr.tvz.firechat.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import hr.tvz.firechat.App
import hr.tvz.firechat.R
import hr.tvz.firechat.ui.chat.ChatFragment
import hr.tvz.firechat.ui.profile.UserProfileFragment
import hr.tvz.firechat.ui.userlist.UserListFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract.View {

    @Inject lateinit var mainPresenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbarMain)

        (application as App).component.inject(this)
        mainPresenter.attach(this)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_messages -> {
                    showMessageView()
                    true
                }
                R.id.navigation_people -> {
                    showUserListView()
                    true
                }
                R.id.navigation_profile -> {
                    showProfileView()
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.navigation_messages
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                onLogoutClick()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showMessageView() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.containerMain, ChatFragment.newInstance(mainPresenter.getCurrentUserId()))
                .commit()
    }

    override fun showUserListView() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.containerMain, UserListFragment.newInstance())
                .commit()
    }

    override fun showProfileView() {

        supportFragmentManager.beginTransaction()
                .replace(R.id.containerMain, UserProfileFragment.newInstance(mainPresenter.getCurrentUserId()))
                .commit()
    }

    override fun onLogoutClick() {
        mainPresenter.logoutUser()
        finish()
    }
}