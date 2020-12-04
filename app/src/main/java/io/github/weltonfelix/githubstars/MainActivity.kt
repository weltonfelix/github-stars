 package io.github.weltonfelix.githubstars

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response

 class MainActivity : AppCompatActivity() {
	 private lateinit var users: MutableList<User>
	 private lateinit var userAdapter: UserAdapter

	 override fun onCreate(savedInstanceState: Bundle?) {
		 super.onCreate(savedInstanceState)
		 setContentView(R.layout.activity_main)

		 users = SaveData(this).users
		 userAdapter = UserAdapter(users)

		 val addUserButton: Button = findViewById(R.id.search_user_button)

		 addUserButton.setOnClickListener {
			 val usernameData = search_user

			 AddUser(
				 this,
				 usernameData,
				 addUserButton,
				 "Buscando usuário...",
				 "Usuário não encontrado",
				 userAdapter
			 )
		 }

		 users_list.layoutManager = LinearLayoutManager(this)
		 users_list.adapter = userAdapter
	 }

	open class AddUser() {
		constructor(
			context: Context,
			usernameData: TextInputEditText,
			button: Button,
			text: String,
			failText: String,
			adapter: UserAdapter
		) : this() {
			button.isEnabled = false

			val snackbar = Snackbar.make(button, text, Snackbar.LENGTH_INDEFINITE)

			snackbar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.primary))
			snackbar.show()

			val userResponse = search(usernameData.text.toString(), snackbar, button)

			if (userResponse?.isSuccessful == false) {
				val failSnackbar = Snackbar.make(button, failText, Snackbar.LENGTH_SHORT)
				failSnackbar.setAction(
					"Tentar novamente",
					SnackBarTryAgainListener(context, usernameData, button, text, failText, adapter)
				)
				failSnackbar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.alert))
				failSnackbar.setActionTextColor(Color.WHITE)
				failSnackbar.show()
			} else {
				val user = userResponse?.body()
				if (user != null) {
					val newUser = User(user.name, user.bio, user.avatar_url, user.login)
					adapter.update(newUser)
					usernameData.setText("")

					val usersList = SaveData(context).users

					usersList.add(newUser)

					SaveData(context).users = usersList
				}
			}
		}

		private fun search(username: String, snackbar: Snackbar, button: Button): Response<UserData>? {
			val githubService = GithubFactory.makeGithubService()
			var response: Response<UserData>? = null

			runBlocking {
				launch {
					response = githubService.getUser(username)
				}
			}
			snackbar.dismiss()
			button.isEnabled = true

			return response
		}

		class SnackBarTryAgainListener(
			private val context: Context,
			private val usernameData: TextInputEditText,
			private val button: Button,
			private val text: String,
			private val failText: String,
			private val adapter: UserAdapter
		) : View.OnClickListener {
			override fun onClick(v: View) {
				AddUser(context, usernameData, button, text, failText, adapter)
			}
		}
	}


}
