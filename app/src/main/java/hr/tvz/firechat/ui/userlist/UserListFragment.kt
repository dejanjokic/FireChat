package hr.tvz.firechat.ui.userlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import hr.tvz.firechat.App
import hr.tvz.firechat.R
import hr.tvz.firechat.data.model.User
import hr.tvz.firechat.ui.profile.UserProfileFragment
import hr.tvz.firechat.util.ext.gone
import hr.tvz.firechat.util.ext.visible
import kotlinx.android.synthetic.main.fragment_user_list.*
import javax.inject.Inject

class UserListFragment : Fragment(), UserListContract.View {

    @Inject lateinit var userListPresenter: UserListContract.Presenter

    private val userListAdapter = UserListAdapter { showUserProfile(it.id) }

    companion object {
        fun newInstance(): UserListFragment = UserListFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as App).component.inject(this)
        userListPresenter.attach(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_user_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userListPresenter.loadUsers()

        // TODO: ItemSeparator/Decorator?
        recyclerViewUsers.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
        }
    }

    override fun onDestroy() {
        userListPresenter.detach()
        super.onDestroy()
    }

    override fun showLoading() {
        progressBarUserList.visible()
    }

    override fun hideLoading() {
        progressBarUserList.gone()
    }

    override fun showUsers(users: List<User>?) {
        recyclerViewUsers.visible()
        // TODO: Remove hardcoded value. SharedPrefs currentUserInfo?
        val id = FirebaseAuth.getInstance().currentUser?.uid
        userListAdapter.submitList(users?.filter { it.id != id})
    }

    override fun showUserProfile(userId: String?) {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.containerMain, UserProfileFragment.newInstance(userId))
            .commit()
    }
}