package com.example.qla_cimol.Orders
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.qla_cimol.R
import com.example.qla_cimol.RatingActivity

class OrdersFragment : Fragment() {

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

        // Menghubungkan tombol Rating
        val btnRating = view.findViewById<Button>(
            R.id.btnBeriRating
        )

        // Membuka halaman RatingActivity
        btnRating.setOnClickListener {

            val intent = Intent(
                requireContext(),
                RatingActivity::class.java
            )

            startActivity(intent)
        }
    }
}
