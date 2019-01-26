package hr.tvz.firechat.ui.userlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import hr.tvz.firechat.R
import hr.tvz.firechat.data.model.User
import hr.tvz.firechat.util.glide.GlideApp
import kotlinx.android.synthetic.main.list_item_user.view.*

class UserListAdapter(private val clickListener: (User) -> Unit) : ListAdapter<User, UserListAdapter.ViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_user, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: User, clickListener: (User) -> Unit) = with(itemView) {
            textViewUserItemName.text = user.displayName ?: user.email

            GlideApp.with(itemView)
                .load(user.profilePicturePath)
                .transforms(CenterCrop(), RoundedCorners(360))
                .placeholder(R.drawable.ic_person)
                .into(imageViewUserItemAvatar)

            setOnClickListener { clickListener(user) }
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem
    }
}