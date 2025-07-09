package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FakeNetworkAdapter(
    private val networks: List<FakeNetwork>,
    private val onConnectClicked: (FakeNetwork) -> Unit
) : RecyclerView.Adapter<FakeNetworkAdapter.FakeNetworkViewHolder>() {

    inner class FakeNetworkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ssidText: TextView = itemView.findViewById(R.id.tvSsid)
        val bssidText: TextView = itemView.findViewById(R.id.tvBssid)
        val rssiText: TextView = itemView.findViewById(R.id.tvRssi)
        val btnConnect: Button = itemView.findViewById(R.id.btnConnect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FakeNetworkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fake_network, parent, false)
        return FakeNetworkViewHolder(view)
    }

    override fun onBindViewHolder(holder: FakeNetworkViewHolder, position: Int) {
        val network = networks[position]
        holder.ssidText.text = "SSID: ${network.ssid}"
        holder.bssidText.text = "BSSID: ${network.bssid}"
        holder.rssiText.text = "RSSI: ${network.rssi} dBm"
        holder.btnConnect.setOnClickListener {
            onConnectClicked(network)
        }
    }

    override fun getItemCount(): Int = networks.size
}
