package io.github.weltonfelix.githubstars

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

data class UserData(
	val name: String,
	val login: String,
	val bio: String,
	val avatar_url: String,
)

interface GithubService {
	@GET("/users/{user}")
	suspend fun getUser(@Path("user") user: String): Response<UserData>
}