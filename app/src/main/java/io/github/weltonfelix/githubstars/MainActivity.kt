package io.github.weltonfelix.githubstars

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val addUserButton: Button = findViewById(R.id.search_user_button)

		addUserButton.setOnClickListener {
			AddUser(
				this,
				addUserButton,
				"Buscando usuário...",
				"Usuário não encontrado"
			)
		}
	}

	open class AddUser() {
		constructor(context: Context, button: Button, text: String, failText: String) : this() {
			button.isEnabled = false

			val snackbar = Snackbar.make(button, text, Snackbar.LENGTH_INDEFINITE)

			snackbar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.primary))
			snackbar.show()

			search(snackbar, button)
			val fail_snackbar = Snackbar.make(button, failText, Snackbar.LENGTH_SHORT)
			fail_snackbar.setAction(
				"Tentar novamente",
				SnackBarTryAgainListener(context, button, text, failText)
			)
			fail_snackbar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.alert))
			fail_snackbar.setActionTextColor(Color.WHITE)
			fail_snackbar.show()

		}

		private fun search(snackbar: Snackbar, button: Button) {
			Handler().postDelayed({
				snackbar.dismiss()
				button.isEnabled = true
			}, 2000)
		}

		class SnackBarTryAgainListener(
			private val context: Context,
			private val button: Button,
			private val text: String,
			private val failText: String
		) : View.OnClickListener {
			override fun onClick(v: View) {
				AddUser(context, button, text, failText)
			}
		}
	}


}
