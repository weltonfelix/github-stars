package io.github.weltonfelix.githubstars

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class UserAdapter(users_list: MutableList<User>, private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	private val users: MutableList<User> = users_list

	override fun getItemCount(): Int {
		return users.size
	}

	fun update(user: User) {
		users.add(user)
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val userCard = LayoutInflater
			.from(parent.context)
			.inflate(R.layout.user_card, parent, false)

		return UserViewHolder(userCard)
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		val currentUser = users[position]
		if (holder is UserViewHolder) {
			holder.userName.text = currentUser.name
			holder.userBio.text = currentUser.bio
			holder.userProfileButton.setOnClickListener {
				val userData = Bundle()
				userData.putString("userName", currentUser.name)
				userData.putString("userLogin", currentUser.login)
				userData.putString("userBio", currentUser.bio)
				userData.putString("userPic", currentUser.picture)

				val userProfile = Intent(context, UserActivity::class.java).putExtras(userData)
				startActivity(context, userProfile, null)
			}
			Picasso
				.get()
				.load(currentUser.picture)
				.fit()
				.transform(CropCircleTransformation())
				.placeholder(R.drawable.user_picture_background)
				.into(holder.userPicture)
		}
	}

	class UserViewHolder(userView: View) : RecyclerView.ViewHolder(userView) {
		val userName: TextView = itemView.findViewById(R.id.user_name)
		val userBio: TextView = itemView.findViewById(R.id.user_bio)
		val userPicture: ImageView = itemView.findViewById(R.id.user_picture)
		val userProfileButton: Button = itemView.findViewById(R.id.user_view_profile)
	}
}