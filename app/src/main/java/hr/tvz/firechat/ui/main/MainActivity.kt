package hr.tvz.firechat.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import hr.tvz.firechat.App
import hr.tvz.firechat.R
import hr.tvz.firechat.data.interactor.AuthUserInteractor
import hr.tvz.firechat.data.model.User
import hr.tvz.firechat.ui.chat.ChatFragment
import hr.tvz.firechat.ui.profile.UserProfileFragment
import hr.tvz.firechat.ui.userlist.UserListFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var firebaseAuthUserInteractor: AuthUserInteractor

    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbarMain)

        (application as App).component.inject(this)
        currentUser = firebaseAuthUserInteractor.getCurrentUser()


        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_messages -> {
                    // TODO: ID
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.containerMain, ChatFragment.newInstance())
                        .commit()
                    true
                }
                R.id.navigation_people -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.containerMain, UserListFragment.newInstance())
                        .commit()
                    true
                }
                R.id.navigation_profile -> {
                    // TODO: SharedPrefs ID
                    val id = FirebaseAuth.getInstance().currentUser?.uid
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.containerMain, UserProfileFragment.newInstance(id))
                        .commit()
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
                firebaseAuthUserInteractor.logoutUser()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}