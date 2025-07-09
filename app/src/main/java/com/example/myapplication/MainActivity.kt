package com.example.myapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var rvNetworks: RecyclerView
    private lateinit var tvStatus: TextView

    private val fakeNetworks = listOf(
        FakeNetwork("Free_Public_WiFi", "00:00:12:34:56:78", -89),
        FakeNetwork("xfinitywifi", "00:11:22:33:44:55", -70),
        FakeNetwork("JAMH_Clone", "ff:ff:ff:ff:ff:ff", -90),
        FakeNetwork("Hotel_NET", "aa:bb:cc:dd:ee:ff", -60),
        FakeNetwork("Open_HackZone", "00:00:00:87:45:91", -85)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvNetworks = findViewById(R.id.rvNetworks)
        tvStatus = findViewById(R.id.tvStatus)

        rvNetworks.layoutManager = LinearLayoutManager(this)
        rvNetworks.adapter = FakeNetworkAdapter(fakeNetworks) { network ->
            val anomalies = detectAnomalies(network)
            val summary = buildString {
                append("📡 Connected to: ${network.ssid}\n")
                append("🔗 BSSID: ${network.bssid}\n")
                append("📶 RSSI: ${network.rssi} dBm\n\n")
                if (anomalies.isEmpty()) {
                    append("✅ No anomalies found.")
                } else {
                    anomalies.forEach { append("$it\n") }
                }
            }
            tvStatus.text = summary.trim()
        }
    }

    private fun detectAnomalies(network: FakeNetwork): List<String> {
        val anomalies = mutableListOf<String>()

        if (network.rssi < -85) {
            anomalies.add("🔻 Weak signal: ${network.rssi} dBm")
        }
        if (network.bssid.startsWith("00:00") || network.bssid == "ff:ff:ff:ff:ff:ff") {
            anomalies.add("🛑 Spoofed BSSID: ${network.bssid}")
        }
        if (network.ssid.lowercase().contains("free") || network.ssid.lowercase().contains("public")) {
            anomalies.add("⚠️ Suspicious SSID: \"${network.ssid}\"")
        }

        return anomalies
    }
}
