package com.example.wifi

import android.net.wifi.ScanResult

object WifiAnalyzer {

    fun analyze(result: ScanResult, allResults: List<ScanResult>): AnalyzedNetwork {
        val anomalyReasons = mutableListOf<String>()
        var isAnomaly = false

        if (result.SSID.isNullOrEmpty()) {
            isAnomaly = true
            anomalyReasons.add("Hidden SSID")
        }

        if (allResults.count { it.SSID == result.SSID } > 1) {
            isAnomaly = true
            anomalyReasons.add("Duplicate SSID (possible fake AP)")
        }

        if (result.level < -70) {
            isAnomaly = true
            anomalyReasons.add("Weak Signal")
        }

        if (result.level > -30) {
            isAnomaly = true
            anomalyReasons.add("Suspiciously Strong Signal")
        }

        if (!result.capabilities.contains("WEP", true) &&
            !result.capabilities.contains("WPA", true)
        ) {
            isAnomaly = true
            anomalyReasons.add("Open Network")
        }

        if (result.capabilities.contains("WPS", true)) {
            isAnomaly = true
            anomalyReasons.add("WPS Enabled")
        }

        return AnalyzedNetwork(
            ssid = result.SSID ?: "<unknown>",
            bssid = result.BSSID ?: "<unknown>",
            authType = result.capabilities ?: "Unknown",
            level = result.level,
            isAnomaly = isAnomaly,
            anomalyReasons = anomalyReasons
        )
    }
}

