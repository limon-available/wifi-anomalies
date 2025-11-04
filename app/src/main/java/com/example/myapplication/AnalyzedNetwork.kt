package com.example.wifi

data class AnalyzedNetwork(
    val ssid: String,
    val bssid: String,
    val authType: String,
    val level: Int,
    val isAnomaly: Boolean = false,
    val anomalyReasons: List<String> = emptyList()
)
