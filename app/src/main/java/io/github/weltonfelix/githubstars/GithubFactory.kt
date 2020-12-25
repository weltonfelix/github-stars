package io.github.weltonfelix.githubstars

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object GithubFactory {
	private const val BASE_URL = "https://api.github.com"

	fun makeGithubService(): GithubService = Retrofit
		.Builder()
		.baseUrl(BASE_URL)
		.addConverterFactory(MoshiConverterFactory.create())
		.build()
		.create(GithubService::class.java)

	fun makeReposService(): ReposService = Retrofit
		.Builder()
		.baseUrl(BASE_URL)
		.addConverterFactory(MoshiConverterFactory.create())
		.build()
		.create(ReposService::class.java)
}