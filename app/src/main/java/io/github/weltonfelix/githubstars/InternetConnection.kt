package io.github.weltonfelix.githubstars

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class InternetConnection(private val context: Context) {
	fun isConnected(): Boolean {
		val connection = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val networkCapabilities = connection.activeNetwork?: return false
		val activeNetwork = connection.getNetworkCapabilities(networkCapabilities)?: return false

		return when {
			activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
			activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
			activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
			else -> false
		}
	}
}