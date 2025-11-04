package com.example.wifi

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AnomalySolutionActivity : AppCompatActivity() {

    private val solutionsMap = mapOf(
        "Open Network" to "🔓 [বাংলা] রাউটার Settings এ গিয়ে WPA2/WPA3 encryption চালু করুন。\n[EN] Enable WPA2/WPA3 encryption in your router.",
        "Weak Signal" to "📶 [বাংলা] রাউটার কাছে যান বা Wi-Fi এক্সটেন্ডার ব্যবহার করুন。\n[EN] Move closer to the router or use a Wi-Fi extender.",
        "Hidden SSID" to "👻 [বাংলা] রাউটারে SSID unhide করুন আরও নিরাপত্তার জন্য。\n[EN] Unhide SSID in your router for better security.",
        "WPS Enabled" to "⚠️ [বাংলা] WPS ফিচার বন্ধ করুন。\n[EN] Disable WPS feature from router settings.",
        "Suspiciously Strong Signal" to "🚨 [বাংলা] সম্ভবত Fake Access Point, কানেক্ট না করা উত্তম。\n[EN] May be a Fake AP, avoid connecting.",
        "Duplicate SSID (possible fake AP)" to "🎭 [বাংলা] একই নামের একাধিক Wi-Fi থাকলে সাবধান。\n[EN] Multiple same SSID detected, one may be fake."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anomaly_solution)

        val ssid = intent.getStringExtra("ssid") ?: ""
        val reasons = intent.getStringArrayListExtra("reasons") ?: arrayListOf()

        val txtNetwork: TextView = findViewById(R.id.txtNetwork)
        val txtSolution: TextView = findViewById(R.id.txtSolution)

        txtNetwork.text = "Network: $ssid"
        txtSolution.text = reasons
            .map { solutionsMap[it] ?: "❓ Solution not found." }
            .joinToString("\n\n")
    }
}

