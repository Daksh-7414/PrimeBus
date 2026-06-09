package com.example.primebus

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    private fun isNetworkAvailable(
        connectivityManager: ConnectivityManager
    ): Boolean {

        val network =
            connectivityManager.activeNetwork ?: return false

        val capabilities =
            connectivityManager.getNetworkCapabilities(network)
                ?: return false

        return capabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        ) &&
                capabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )
    }

    val isOnline: StateFlow<Boolean> = callbackFlow {

        val connectivityManager =
            context.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager

        val callback =
            object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    trySend(
                        isNetworkAvailable(connectivityManager)
                    )
                }

                override fun onLost(network: Network) {
                    trySend(
                        isNetworkAvailable(connectivityManager)
                    )
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    trySend(
                        isNetworkAvailable(connectivityManager)
                    )
                }

                override fun onUnavailable() {
                    trySend(false)
                }
            }

        val request = NetworkRequest.Builder()
            .addCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            )
            .build()

        connectivityManager.registerNetworkCallback(
            request,
            callback
        )

        trySend(
            isNetworkAvailable(connectivityManager)
        )

        awaitClose {
            connectivityManager.unregisterNetworkCallback(
                callback
            )
        }

    }
        .distinctUntilChanged()
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )
}