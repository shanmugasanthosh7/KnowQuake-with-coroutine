package com.sample.knowquake.network

import java.io.IOException

class NoNetworkException : IOException() {

    override val message: String?
        get() = "No network connection. Check WiFi or mobile network connectivity"
}