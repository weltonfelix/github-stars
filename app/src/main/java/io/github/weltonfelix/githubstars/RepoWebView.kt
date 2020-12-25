package io.github.weltonfelix.githubstars

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.AppCompatTextView
import io.github.weltonfelix.githubstars.databinding.ActivityRepoWebViewBinding

class RepoWebView : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val binding = ActivityRepoWebViewBinding.inflate(layoutInflater)
		val view = binding.root
		setContentView(view)

		val webView = binding.webview

		val repoData = intent.extras

		val repoName = repoData?.getString("name")
		val repoAuthor = repoData?.getString("author")

		supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
		supportActionBar?.setCustomView(R.layout.action_bar)
		supportActionBar?.customView?.findViewById<AppCompatTextView>(R.id.action_bar_title)!!.text = repoName
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		webView.loadUrl("https://github.com/$repoAuthor/$repoName")
	}

	override fun onSupportNavigateUp(): Boolean {
		onBackPressed()
		return super.onSupportNavigateUp()
	}
}