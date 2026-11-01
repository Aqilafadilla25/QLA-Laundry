
package com.example.qla_cimol.Home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.qla_cimol.R
import com.example.qla_cimol.PickupActivity

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

        val btnPickup = view.findViewById<Button>(
            R.id.btnPickup
        )

        btnPickup.setOnClickListener {
            val intent = Intent(
                requireContext(),
                PickupActivity::class.java
            )

            startActivity(intent)
        }
    }
}
