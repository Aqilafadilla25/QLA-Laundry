package com.example.qla_cimol

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Tombol Logout
        val btnLogout = findViewById<Button>(R.id.btnlogout)

        btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin mau keluar?")
                .setPositiveButton("Logout") { _, _ ->

                    // Hapus status login
                    val pref = getSharedPreferences(
                        "user_pref",
                        MODE_PRIVATE
                    )
                    pref.edit()
                        .putBoolean("isLogin", false)
                        .apply()

                    // Kembali ke halaman Login
                    val intent = Intent(
                        this,
                        MainActivity::class.java
                    )
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK

                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }
}
