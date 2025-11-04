 package com.example.wifi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var wifiManager: WifiManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FakeNetworkAdapter

    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) startScan() else showPermissionRationale()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = FakeNetworkAdapter(
            mutableListOf(),
            { network -> showNetworkDetails(network) },
            { network -> showAnomalySolutions(network) }
        )
        recyclerView.adapter = adapter

        findViewById<View>(R.id.btnScan).setOnClickListener {
            checkAndScan()
        }

        checkAndScan()
    }

    private fun checkAndScan() {
        if (!wifiManager.isWifiEnabled) {
            AlertDialog.Builder(this)
                .setTitle("Wi-Fi off")
                .setMessage("Wi-Fi চালু করতে চান?")
                .setPositiveButton("On") { _, _ ->
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }
                .setNegativeButton("Cancel", null)
                .show()
            return
        }

        if (ActivityCompat.checkSelfPermission(this, locationPermission) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(locationPermission)
            return
        }

        startScan()
    }

    private fun showPermissionRationale() {
        AlertDialog.Builder(this)
            .setTitle("Location permission প্রয়োজন")
            .setMessage("Wi-Fi স্ক্যান করার জন্য Android এ Location permission দরকার।")
            .setPositiveButton("Grant") { _, _ ->
                permissionLauncher.launch(locationPermission)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun startScan() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                wifiManager.startScan()
                val results = wifiManager.scanResults

                val analyzed = results.map { result ->
                    WifiAnalyzer.analyze(result, results)
                }

                withContext(Dispatchers.Main) {
                    adapter.updateList(analyzed)
                    Toast.makeText(
                        this@MainActivity,
                        "Scan complete: ${results.size} networks পাওয়া গেছে",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showNetworkDetails(item: AnalyzedNetwork) {
        val msg = StringBuilder()
        msg.append("SSID: ${item.ssid}\n")
        msg.append("BSSID: ${item.bssid}\n")
        msg.append("Signal: ${item.level} dBm\n")
        msg.append("Auth: ${item.authType}\n")
        msg.append(
            "Anomaly: ${
                if (item.isAnomaly) item.anomalyReasons.joinToString(", ")
                else "None"
            }"
        )

        val builder = AlertDialog.Builder(this)
            .setTitle("Network details")
            .setMessage(msg.toString())
            .setPositiveButton("OK", null)

        if (item.isAnomaly) {
            builder.setNeutralButton("View Solutions") { _, _ ->
                showAnomalySolutions(item)
            }
        }

        builder.show()
    }

    private fun showAnomalySolutions(network: AnalyzedNetwork) {
        val intent = Intent(this, AnomalySolutionActivity::class.java)
        intent.putExtra("ssid", network.ssid)
        intent.putStringArrayListExtra("reasons", ArrayList(network.anomalyReasons))
        startActivity(intent)
    }
}
