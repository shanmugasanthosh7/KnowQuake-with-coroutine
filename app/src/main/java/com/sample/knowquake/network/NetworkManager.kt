package com.sample.knowquake.network

import android.util.Log
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.*

object NetworkManager {

    fun internetConnectionAvailable(i: Int): Boolean {
        var inetAddress: InetAddress?
        try {
            val submit = Executors.newSingleThreadExecutor().submit(Callable<InetAddress> {
                try {
                    InetAddress.getByName("google.com")
                } catch (unused: UnknownHostException) {
                    null
                }
            })
            inetAddress = submit.get(i.toLong(), TimeUnit.MILLISECONDS) as InetAddress
            try {
                submit.cancel(true)
            } catch (unused: InterruptedException) {
            } catch (unused: ExecutionException) {
            } catch (unused: TimeoutException) {
            }

        } catch (unused2: InterruptedException) {
            inetAddress = null
        } catch (unused2: ExecutionException) {
            inetAddress = null
        } catch (unused2: TimeoutException) {
            inetAddress = null
        }
        if (inetAddress != null) {
            Log.e("net", inetAddress.hostAddress)
        }
        return !(inetAddress == null || inetAddress.hostAddress == "10.0.0.1")
    }
}
