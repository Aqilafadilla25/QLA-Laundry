
package com.example.qla_cimol

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class VerificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        val otp = findViewById<EditText>(R.id.etOtp)
        val btnVerifikasi =
            findViewById<Button>(R.id.btnVerifikasi)

        val nama = intent.getStringExtra("nama") ?: ""
        val hp = intent.getStringExtra("hp") ?: ""
        val username = intent.getStringExtra("username") ?: ""
        val password = intent.getStringExtra("password") ?: ""

        btnVerifikasi.setOnClickListener {

            if (otp.text.toString().trim() != hp || hp.isEmpty()) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Verifikasi Gagal")
                    .setMessage("Kode OTP salah atau kosong!")
                    .setPositiveButton("OK", null)
                    .show()

                return@setOnClickListener
            }

            // Simpan data akun yang berhasil diverifikasi
            val prefs = getSharedPreferences(
                "UserPrefs",
                MODE_PRIVATE
            )

            prefs.edit()
                .putString("nama", nama)
                .putString("hp", hp)
                .putString("username", username)
                .putString("password", password)
                .apply()

            // Langsung buka halaman Login
            val loginIntent = Intent(
                this,
                MainActivity::class.java
            )

            loginIntent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_NEW_TASK

            startActivity(loginIntent)
            finish()
        }
    }
}
