package com.example.qla_cimol

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LaundryInfoAdapter(
    private val data: List<LaundryInfo>
) : RecyclerView.Adapter<LaundryInfoAdapter.InfoViewHolder>() {

    class InfoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val judul: TextView =
            itemView.findViewById(R.id.tvJudulInfo)

        val deskripsi: TextView =
            itemView.findViewById(R.id.tvDeskripsiInfo)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InfoViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_laundry_info, parent, false)

        return InfoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: InfoViewHolder,
        position: Int
    ) {
        val item = data[position]

        holder.judul.text = item.judul
        holder.deskripsi.text = item.deskripsi
    }

    override fun getItemCount(): Int = data.size
}
