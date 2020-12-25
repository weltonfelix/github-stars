package io.github.weltonfelix.githubstars

data class Owner (val login: String, val avatar_url: String)
data class Repo(var name: String, var owner: Owner, var url: String)