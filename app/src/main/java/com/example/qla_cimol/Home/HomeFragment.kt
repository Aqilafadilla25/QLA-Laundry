
package com.example.qla_cimol.Home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.qla_cimol.LaundryService
import com.example.qla_cimol.LaundryServiceAdapter
import com.example.qla_cimol.LaundryInfo
import com.example.qla_cimol.LaundryInfoAdapter
import com.example.qla_cimol.PickupActivity
import com.example.qla_cimol.R
import com.example.qla_cimol.RatingActivity

class HomeFragment : Fragment() {

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

        // DATA LAYANAN LAUNDRY
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

        // CUSTOM ADAPTER
        val gridFitur = view.findViewById<GridView>(R.id.gridFitur)

        gridFitur.adapter = LaundryServiceAdapter(
            requireContext(),
            dataLayanan
        )

        // KLIK LAYANAN
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

        // DATA INFORMASI LAUNDRY
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

        // MENGHUBUNGKAN RECYCLERVIEW
        val rvLaundryInfo =
            view.findViewById<RecyclerView>(R.id.rvLaundryInfo)

        rvLaundryInfo.layoutManager =
            LinearLayoutManager(requireContext())

        rvLaundryInfo.adapter =
            LaundryInfoAdapter(informasiLaundry)
    }
}
