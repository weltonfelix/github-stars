package io.github.weltonfelix.githubstars

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SaveData(context: Context) {
	private val sharedPreferences: SharedPreferences by lazy {
		context.getSharedPreferences("users", Context.MODE_PRIVATE)
	}

	private val sharedPreferencesEditor = sharedPreferences.edit()

	var users: MutableList<User>
		get() {
			val raw = sharedPreferences.getString("users", null) ?: return mutableListOf()

			val sType = object : TypeToken<MutableList<User>>() {}.type

			return Gson().fromJson(raw, sType)
		}
		set(userList) {
			val usersJSON = Gson().toJson(userList)

			sharedPreferencesEditor.putString("users", usersJSON).apply()
		}
}