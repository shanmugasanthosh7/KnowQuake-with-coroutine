package com.sample.knowquake.base

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import com.aptus.droidils.utils.getConnectivityManager
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity() {

    companion object {
        private const val TAG = "BaseActivity"
    }

    abstract fun onAvailable(network: Network)

    abstract fun onUnavailable()

    abstract fun onLost(network: Network)

    /**
     * We can use this replacement of network connectivity broadcast receiver.
     */
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.d(TAG, "Connection Available")
            this@BaseActivity.onAvailable(network)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            Log.d(TAG, "Connection Unavailable")
            this@BaseActivity.onUnavailable()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Log.d(TAG, "Connection lost")
            this@BaseActivity.onLost(network)
        }
    }

    override fun onResume() {
        super.onResume()
        val request = NetworkRequest.Builder()
            .build()
        getConnectivityManager().registerNetworkCallback(request, networkCallback)
    }

    override fun onPause() {
        super.onPause()
        getConnectivityManager().unregisterNetworkCallback(networkCallback)
    }

}