package com.example.qla_cimol.Home
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qla_cimol.R
import com.example.qla_cimol.data.model.ServiceModel
class ServiceCrudAdapter(
    private val listService: List<ServiceModel>,
    private val onEditClick: (ServiceModel) -> Unit,
    private val onDeleteClick: (ServiceModel) -> Unit
) : RecyclerView.Adapter<ServiceCrudAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvNamaLayanan)
        val tvHarga: TextView = view.findViewById(R.id.tvHargaEstimasi)
        val btnEdit: Button = view.findViewById(R.id.btnEditService)
        val btnHapus: Button = view.findViewById(R.id.btnHapusService)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service_crud, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listService[position]
        holder.tvNama.text = item.nama_layanan
        holder.tvHarga.text = "Rp ${item.harga_per_kg} / kg • Estimasi ${item.estimasi_jam} Jam"

        holder.btnEdit.setOnClickListener { onEditClick(item) }
        holder.btnHapus.setOnClickListener { onDeleteClick(item) }
    }
    override fun getItemCount(): Int = listService.size
}