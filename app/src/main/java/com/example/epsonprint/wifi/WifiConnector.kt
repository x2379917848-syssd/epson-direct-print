package com.example.epsonprint.wifi

import android.content.Context
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSuggestion
import android.text.TextUtils

class WifiConnector(private val context: Context) {

    private val wifiManager: WifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    // Submit a Wi‑Fi suggestion. System may show a notification/panel to connect.
    fun addSuggestion(ssid: String, password: String?): Boolean {
        val builder = WifiNetworkSuggestion.Builder().setSsid(ssid)
        if (!password.isNullOrEmpty()) {
            builder.setWpa2Passphrase(password) // Most Epson hotspots use WPA2 PSK
        }
        val suggestion = builder.build()
        val status = wifiManager.addNetworkSuggestions(listOf(suggestion))
        return status == WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS
    }

    // Get current connected SSID, requires location permission on Android 10+
    fun currentSsid(): String? {
        val info: WifiInfo = wifiManager.connectionInfo ?: return null
        val ssid = info.ssid ?: return null
        return normalizeQuotedSsid(ssid)
    }

    private fun normalizeQuotedSsid(ssid: String): String {
        return if (!TextUtils.isEmpty(ssid) && ssid.startsWith("\"") && ssid.endsWith("\"")) {
            ssid.substring(1, ssid.length - 1)
        } else ssid
    }
}
