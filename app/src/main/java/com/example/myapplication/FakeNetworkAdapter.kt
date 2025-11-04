package com.example.wifi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FakeNetworkAdapter(
    private var items: MutableList<AnalyzedNetwork>,
    private val onConnectClick: (AnalyzedNetwork) -> Unit,
    private val onViewSolutionsClick: (AnalyzedNetwork) -> Unit
) : RecyclerView.Adapter<FakeNetworkAdapter.NetworkViewHolder>() {

    inner class NetworkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtSsid: TextView = view.findViewById(R.id.txtSsid)

        val txtAnomaly: TextView = view.findViewById(R.id.txtAnomaly)
        val btnViewSolutions: Button = view.findViewById(R.id.btnViewSolutions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetworkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fake_network, parent, false)
        return NetworkViewHolder(view)
    }

    override fun onBindViewHolder(holder: NetworkViewHolder, position: Int) {
        val item = items[position]
        holder.txtSsid.text = item.ssid


        if (item.isAnomaly) {
            holder.txtAnomaly.visibility = View.VISIBLE
            holder.txtAnomaly.text = "Anomaly: ${item.anomalyReasons.joinToString(", ")}"
            holder.btnViewSolutions.visibility = View.VISIBLE
            holder.btnViewSolutions.setOnClickListener { onViewSolutionsClick(item) }
        } else {
            holder.txtAnomaly.visibility = View.GONE
            holder.btnViewSolutions.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: List<AnalyzedNetwork>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}



