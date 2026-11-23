package com.example.qla_cimol.Orders
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qla_cimol.R
import com.example.qla_cimol.data.model.OrderModel
class OrderCrudAdapter(
    private val listOrder: List<OrderModel>,
    private val onEditClick: (OrderModel) -> Unit,
    private val onDeleteClick: (OrderModel) -> Unit
) : RecyclerView.Adapter<OrderCrudAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvNamaPelangganOrder)
        val tvDetail: TextView = view.findViewById(R.id.tvDetailOrder)
        val tvStatus: TextView = view.findViewById(R.id.tvStatusOrder)
        val btnEdit: Button = view.findViewById(R.id.btnEditOrder)
        val btnHapus: Button = view.findViewById(R.id.btnHapusOrder)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_crud, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOrder[position]
        holder.tvNama.text = item.nama_pelanggan
        holder.tvDetail.text = "${item.jenis_layanan} • ${item.berat_kg} kg"
        holder.tvStatus.text = "Status: ${item.status}"

        holder.btnEdit.setOnClickListener { onEditClick(item) }
        holder.btnHapus.setOnClickListener { onDeleteClick(item) }
    }
    override fun getItemCount(): Int = listOrder.size
}