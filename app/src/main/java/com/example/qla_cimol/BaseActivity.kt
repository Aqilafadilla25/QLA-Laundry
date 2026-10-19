
package com.example.qla_cimol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.qla_cimol.databinding.ActivityBaseBinding
import com.example.qla_cimol.Home.HomeFragment
import com.example.qla_cimol.Orders.OrdersFragment
import com.example.qla_cimol.History.HistoryFragment
import com.example.qla_cimol.Notifications.NotificationsFragment
import com.example.qla_cimol.Profile.ProfileFragment

class BaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        binding.bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.orders -> {
                    replaceFragment(OrdersFragment())
                    true
                }

                R.id.history -> {
                    replaceFragment(HistoryFragment())
                    true
                }

                R.id.notifications -> {
                    replaceFragment(NotificationsFragment())
                    true
                }

                R.id.profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }
}
