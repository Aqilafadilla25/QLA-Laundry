package com.example.qla_cimol.Orders

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qla_cimol.R
import com.example.qla_cimol.RatingActivity
import com.example.qla_cimol.data.api.SupabaseApiClient
import com.example.qla_cimol.data.model.OrderModel
import kotlinx.coroutines.launch

class OrdersFragment : Fragment() {

    private lateinit var rvOrders: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_orders,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        rvOrders = view.findViewById(R.id.rvOrders)
        rvOrders.layoutManager = LinearLayoutManager(requireContext())

        // Tombol Pilih Layanan -> Buka Dialog Tambah Pesanan (CREATE / POST)
        view.findViewById<Button>(R.id.btnPesanReguler).setOnClickListener {
            showFormOrderDialog(null, "Cuci Reguler")
        }
        view.findViewById<Button>(R.id.btnPesanSetrika).setOnClickListener {
            showFormOrderDialog(null, "Cuci + Setrika")
        }
        view.findViewById<Button>(R.id.btnPesanBedcover).setOnClickListener {
            showFormOrderDialog(null, "Cuci Bedcover")
        }

        // Memuat daftar pesanan dari Supabase (READ / GET)
        loadOrders()

        // Tombol Rating
        val btnRating = view.findViewById<Button>(R.id.btnBeriRating)
        btnRating.setOnClickListener {
            val intent = Intent(requireContext(), RatingActivity::class.java)
            startActivity(intent)
        }
    }

    // READ (GET)
    private fun loadOrders() {
        lifecycleScope.launch {
            try {
                val listOrder = SupabaseApiClient.apiService.getOrders()
                rvOrders.adapter = OrderCrudAdapter(
                    listOrder = listOrder,
                    onEditClick = { order ->
                        showFormOrderDialog(order, order.jenis_layanan) // Edit (PATCH)
                    },
                    onDeleteClick = { order ->
                        konfirmasiHapusOrder(order) // Hapus (DELETE)
                    }
                )
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Gagal memuat pesanan: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // CREATE (POST) & UPDATE (PATCH)
    private fun showFormOrderDialog(orderEdit: OrderModel?, defaultLayanan: String) {
        val isEdit = orderEdit != null
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(if (isEdit) "Edit Pesanan" else "Buat Pesanan ($defaultLayanan)")

        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 30, 50, 10)
        }

        val etNama = EditText(requireContext()).apply {
            hint = "Nama Pelanggan"
            setText(orderEdit?.nama_pelanggan ?: "")
        }

        val etLayanan = EditText(requireContext()).apply {
            hint = "Jenis Layanan"
            setText(orderEdit?.jenis_layanan ?: defaultLayanan)
        }

        val etBerat = EditText(requireContext()).apply {
            hint = "Berat (kg), misal: 3.5"
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            setText(orderEdit?.berat_kg?.toString() ?: "")
        }

        val etStatus = EditText(requireContext()).apply {
            hint = "Status (Diproses / Selesai)"
            setText(orderEdit?.status ?: "Diproses")
        }

        layout.addView(etNama)
        layout.addView(etLayanan)
        layout.addView(etBerat)
        layout.addView(etStatus)
        builder.setView(layout)

        builder.setPositiveButton("Simpan") { _, _ ->
            val nama = etNama.text.toString().trim()
            val layanan = etLayanan.text.toString().trim()
            val berat = etBerat.text.toString().toDoubleOrNull() ?: 0.0
            val status = etStatus.text.toString().trim()

            if (nama.isEmpty() || layanan.isEmpty() || berat <= 0.0 || status.isEmpty()) {
                Toast.makeText(requireContext(), "Semua data pesanan wajib diisi", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            lifecycleScope.launch {
                try {
                    val dataOrder = OrderModel(
                        nama_pelanggan = nama,
                        jenis_layanan = layanan,
                        berat_kg = berat,
                        status = status
                    )

                    val response = if (isEdit) {
                        SupabaseApiClient.apiService.updateOrder("eq.${orderEdit!!.id}", dataOrder)
                    } else {
                        SupabaseApiClient.apiService.createOrder(dataOrder)
                    }

                    if (response.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            if (isEdit) "Pesanan berhasil diperbarui" else "Pesanan berhasil dibuat",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadOrders()
                    } else {
                        Toast.makeText(requireContext(), "Gagal: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        builder.setNegativeButton("Batal", null)
        builder.show()
    }

    // DELETE
    private fun konfirmasiHapusOrder(order: OrderModel) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Pesanan")
            .setMessage("Hapus pesanan atas nama '${order.nama_pelanggan}'?")
            .setPositiveButton("Hapus") { _, _ ->
                lifecycleScope.launch {
                    try {
                        val response = SupabaseApiClient.apiService.deleteOrder("eq.${order.id}")
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "Pesanan dihapus", Toast.LENGTH_SHORT).show()
                            loadOrders()
                        } else {
                            Toast.makeText(requireContext(), "Gagal hapus: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}