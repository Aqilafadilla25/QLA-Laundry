package com.example.qla_cimol.Home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qla_cimol.LaundryInfo
import com.example.qla_cimol.LaundryInfoAdapter
import com.example.qla_cimol.LaundryService
import com.example.qla_cimol.LaundryServiceAdapter
import com.example.qla_cimol.PickupActivity
import com.example.qla_cimol.R
import com.example.qla_cimol.RatingActivity
import com.example.qla_cimol.data.api.SupabaseApiClient
import com.example.qla_cimol.data.model.ServiceModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var rvServices: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_home,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        // 1. DATA LAYANAN LAUNDRY (GRID)
        val dataLayanan = listOf(
            LaundryService("Cuci Kering", R.drawable.mesincuci),
            LaundryService("Cuci Setrika", R.drawable.setrika),
            LaundryService("Express", R.drawable.express),
            LaundryService(
                "Jemput Laundry",
                android.R.drawable.ic_menu_directions
            ),
            LaundryService(
                "Rating & Ulasan",
                android.R.drawable.btn_star_big_on
            ),
            LaundryService(
                "Pesanan",
                android.R.drawable.ic_menu_agenda
            )
        )

        val gridFitur = view.findViewById<GridView>(R.id.gridFitur)
        gridFitur.adapter = LaundryServiceAdapter(
            requireContext(),
            dataLayanan
        )

        gridFitur.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> Toast.makeText(
                    requireContext(),
                    "Layanan Cuci Kering dipilih",
                    Toast.LENGTH_SHORT
                ).show()

                1 -> Toast.makeText(
                    requireContext(),
                    "Layanan Cuci Setrika dipilih",
                    Toast.LENGTH_SHORT
                ).show()

                2 -> Toast.makeText(
                    requireContext(),
                    "Layanan Express dipilih",
                    Toast.LENGTH_SHORT
                ).show()

                3 -> startActivity(
                    Intent(
                        requireContext(),
                        PickupActivity::class.java
                    )
                )

                4 -> startActivity(
                    Intent(
                        requireContext(),
                        RatingActivity::class.java
                    )
                )

                5 -> Toast.makeText(
                    requireContext(),
                    "Silakan buka tab Pesanan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // 2. CRUD FITUR 1: PAKET LAYANAN EXPRESS (SUPABASE)
        rvServices = view.findViewById(R.id.rvServices)
        rvServices.layoutManager = LinearLayoutManager(requireContext())

        val btnTambahService = view.findViewById<Button>(R.id.btnTambahService)
        btnTambahService.setOnClickListener {
            showFormServiceDialog(null) // null artinya Tambah Baru (POST)
        }

        // Panggil data dari Supabase saat halaman dibuka (READ)
        loadServices()

        // 3. DATA INFORMASI LAUNDRY
        val informasiLaundry = listOf(
            LaundryInfo(
                "Tips Memisahkan Pakaian",
                "Pisahkan pakaian putih dan berwarna sebelum dicuci."
            ),
            LaundryInfo(
                "Tips Merawat Pakaian",
                "Perhatikan label perawatan pakaian sebelum mencuci."
            ),
            LaundryInfo(
                "Menjaga Kebersihan",
                "Gunakan deterjen sesuai jenis dan jumlah pakaian."
            ),
            LaundryInfo(
                "Tips Mengeringkan Pakaian",
                "Keringkan pakaian dengan cara yang sesuai bahannya."
            )
        )

        val rvLaundryInfo = view.findViewById<RecyclerView>(R.id.rvLaundryInfo)
        rvLaundryInfo.layoutManager = LinearLayoutManager(requireContext())
        rvLaundryInfo.adapter = LaundryInfoAdapter(informasiLaundry)
    }

    // READ (GET)
    private fun loadServices() {
        lifecycleScope.launch {
            try {
                val listService = SupabaseApiClient.apiService.getServices()
                rvServices.adapter = ServiceCrudAdapter(
                    listService = listService,
                    onEditClick = { service ->
                        showFormServiceDialog(service) // Edit (PATCH)
                    },
                    onDeleteClick = { service ->
                        konfirmasiHapusService(service) // Hapus (DELETE)
                    }
                )
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Gagal memuat layanan: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // CREATE (POST) & UPDATE (PATCH) DALAM SATU DIALOG
    private fun showFormServiceDialog(serviceEdit: ServiceModel?) {
        val isEdit = serviceEdit != null
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(if (isEdit) "Edit Paket Layanan" else "Tambah Paket Layanan")

        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 30, 50, 10)
        }

        val etNama = EditText(requireContext()).apply {
            hint = "Nama Layanan (misal: Cuci Express 3 Jam)"
            setText(serviceEdit?.nama_layanan ?: "")
        }

        val etHarga = EditText(requireContext()).apply {
            hint = "Harga per Kg (misal: 12000)"
            inputType = InputType.TYPE_CLASS_NUMBER
            setText(serviceEdit?.harga_per_kg?.toString() ?: "")
        }

        val etEstimasi = EditText(requireContext()).apply {
            hint = "Estimasi Jam (misal: 3)"
            inputType = InputType.TYPE_CLASS_NUMBER
            setText(serviceEdit?.estimasi_jam?.toString() ?: "")
        }

        layout.addView(etNama)
        layout.addView(etHarga)
        layout.addView(etEstimasi)
        builder.setView(layout)

        builder.setPositiveButton("Simpan") { _, _ ->
            val nama = etNama.text.toString().trim()
            val harga = etHarga.text.toString().toIntOrNull() ?: 0
            val estimasi = etEstimasi.text.toString().toIntOrNull() ?: 0

            if (nama.isEmpty() || harga <= 0 || estimasi <= 0) {
                Toast.makeText(requireContext(), "Semua field wajib diisi dengan benar", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            lifecycleScope.launch {
                try {
                    val dataService = ServiceModel(
                        nama_layanan = nama,
                        harga_per_kg = harga,
                        estimasi_jam = estimasi
                    )

                    val response = if (isEdit) {
                        SupabaseApiClient.apiService.updateService("eq.${serviceEdit!!.id}", dataService)
                    } else {
                        SupabaseApiClient.apiService.createService(dataService)
                    }

                    if (response.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            if (isEdit) "Layanan berhasil diubah" else "Layanan berhasil ditambahkan",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadServices() // Refresh daftar otomatis
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
    private fun konfirmasiHapusService(service: ServiceModel) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Layanan")
            .setMessage("Yakin ingin menghapus paket '${service.nama_layanan}'?")
            .setPositiveButton("Hapus") { _, _ ->
                lifecycleScope.launch {
                    try {
                        val response = SupabaseApiClient.apiService.deleteService("eq.${service.id}")
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "Layanan berhasil dihapus", Toast.LENGTH_SHORT).show()
                            loadServices() // Refresh daftar otomatis
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