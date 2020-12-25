package io.github.weltonfelix.githubstars

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReposService {
	@GET("/users/{user}/starred")
	suspend fun getRepos(@Path("user") user: String, @Query("page") page: Int): Response<MutableList<Repo>>
}