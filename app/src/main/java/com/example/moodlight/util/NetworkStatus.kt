package com.example.moodlight.util

import android.content.Context
import android.net.ConnectivityManager

@Suppress("DEPRECATION")
class NetworkStatus {
    companion object {
        public val TYPE_WIFI : Int = 1
        public val TYPE_MOBILE : Int = 2
        public val TYPE_NOT_CONNECTED = 3

        public fun getConnectivityStatus(context: Context) : Int {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val networkInfo = manager.activeNetworkInfo
            if (networkInfo != null) {
                val type = networkInfo.type
                if (type == ConnectivityManager.TYPE_MOBILE) {
                    return TYPE_MOBILE
                } else if (type == ConnectivityManager.TYPE_WIFI) {
                    return TYPE_WIFI
                }
            }
            return TYPE_NOT_CONNECTED
        }
    }


}