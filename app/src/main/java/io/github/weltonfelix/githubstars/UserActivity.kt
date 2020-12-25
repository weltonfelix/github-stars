package io.github.weltonfelix.githubstars

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import io.github.weltonfelix.githubstars.databinding.ActivityUserBinding
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.*
import retrofit2.Response

class UserActivity : AppCompatActivity() {
	private var repos: MutableList<Repo> = mutableListOf()
	private lateinit var reposAdapter: ReposAdapter
	private lateinit var binding: ActivityUserBinding
	private var page = 1
	private lateinit var recyclerView: RecyclerView


	private var userLogin: String? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityUserBinding.inflate(layoutInflater)
		val view = binding.root
		setContentView(view)

		val userData = intent.extras

		val userName = userData?.getString("userName")
		userLogin = userData?.getString("userLogin")
		val userBio = userData?.getString("userBio")
		val userPic = userData?.getString("userPic")

		supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
		supportActionBar?.setCustomView(R.layout.action_bar)
		supportActionBar?.customView?.findViewById<AppCompatTextView>(R.id.action_bar_title)!!.text = userName
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		binding.userName.text = userName
		binding.userBio.text = userBio

		Picasso
			.get()
			.load(userPic)
			.fit()
			.transform(CropCircleTransformation())
			.placeholder(R.drawable.user_picture_background)
			.into(binding.userPicture)

		recyclerView = binding.repoList

		reposAdapter = ReposAdapter(mutableListOf(), this@UserActivity)

		binding.repoList.adapter = reposAdapter
	}

	override fun onResume() {
		super.onResume()

		if (InternetConnection(this).isConnected() && userLogin != null) {
			window.decorView.post{
				load()
			}
		}
		else {
			val noConnectionSnackBar = Snackbar.make(binding.root, "Sem conexão com a internet", Snackbar.LENGTH_SHORT)
			noConnectionSnackBar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray))
			noConnectionSnackBar.show()
		}
	}

	override fun onSupportNavigateUp(): Boolean {
		onBackPressed()
		return super.onSupportNavigateUp()
	}

	private fun load() {
		runBlocking {
			launch {
				loadRepos(userLogin!!, page)
			}
		}
		reposAdapter = ReposAdapter(repos, this@UserActivity)
		binding.repoList.adapter = reposAdapter
	}

	private suspend fun loadRepos(userLogin: String, page: Int) {
		var currentPage = page
		var newRepos: MutableList<Repo>?
		do {
			val reposService = GithubFactory.makeReposService()

			var response : Response<MutableList<Repo>>? = null

			coroutineScope {
				launch {
					response = reposService.getRepos(userLogin,currentPage)
				}
			}
			currentPage += 1

			newRepos = response?.body()

			if (newRepos != null) {
				if (newRepos.size > 0) {
					repos.addAll(response?.body()!!)
				}
			}
		} while(newRepos != null && newRepos.size != 0)
	}
}
