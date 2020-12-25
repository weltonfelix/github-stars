package io.github.weltonfelix.githubstars

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class ReposAdapter(private val repos: MutableList<Repo>, private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	override fun getItemCount(): Int {
		return repos.size
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val repoCard = LayoutInflater
			.from(parent.context)
			.inflate(R.layout.repo_card, parent, false)

		return ReposViewHolder(repoCard)
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		val currentRepo = repos[holder.adapterPosition]
		if (holder is ReposViewHolder) {
			holder.repoTitle.text = currentRepo.name
			holder.repoAuthor.text = currentRepo.owner.login
			holder.itemView.setOnClickListener{
				val repoData = Bundle()
				repoData.putString("name", currentRepo.name)
				repoData.putString("author", currentRepo.owner.login)

				val repoPage = Intent(context, RepoWebView::class.java).putExtras(repoData)
				ContextCompat.startActivity(context, repoPage, null)
			}

			Picasso
				.get()
				.load(currentRepo.owner.avatar_url)
				.fit()
				.transform(CropCircleTransformation())
				.placeholder(R.drawable.user_picture_background)
				.into(holder.ownerAvatar)
		}
	}

	class ReposViewHolder(reposView: View) : RecyclerView.ViewHolder(reposView) {
		val repoTitle: TextView = itemView.findViewById(R.id.repo_title)
		val repoAuthor: TextView = itemView.findViewById(R.id.repo_author)
		val ownerAvatar: ImageView = itemView.findViewById(R.id.owner_avatar)
	}
}