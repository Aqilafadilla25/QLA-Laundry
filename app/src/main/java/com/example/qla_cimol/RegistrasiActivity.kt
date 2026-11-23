package com.example.qla_cimol

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.qla_cimol.data.api.SupabaseApiClient
import com.example.qla_cimol.data.model.UserModel
import kotlinx.coroutines.launch

class RegistrasiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi)

        val nama = findViewById<EditText>(R.id.etNama)
        val hp = findViewById<EditText>(R.id.etHandphone)
        val username = findViewById<EditText>(R.id.etUsername)
        val password = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val namaValue = nama.text.toString().trim()
            val hpValue = hp.text.toString().trim()
            val usernameValue = username.text.toString().trim()
            val passwordValue = password.text.toString()

            if (namaValue.isEmpty() || hpValue.isEmpty() ||
                usernameValue.isEmpty() || passwordValue.isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Semua data wajib diisi",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Matikan tombol sementara agar tidak double-submit saat loading
            btnRegister.isEnabled = false

            lifecycleScope.launch {
                try {
                    val userBaru = UserModel(
                        nama = namaValue,
                        no_hp = hpValue,
                        username = usernameValue,
                        password = passwordValue
                    )

                    val response = SupabaseApiClient.apiService.registerUser(userBaru)

                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@RegistrasiActivity,
                            "Registrasi berhasil disimpan ke Supabase!",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@RegistrasiActivity, VerificationActivity::class.java)
                        intent.putExtra("nama", namaValue)
                        intent.putExtra("hp", hpValue)
                        intent.putExtra("username", usernameValue)
                        intent.putExtra("password", passwordValue)
                        startActivity(intent)
                    } else {
                        btnRegister.isEnabled = true
                        Toast.makeText(
                            this@RegistrasiActivity,
                            "Gagal Register (${response.code()}): ${response.errorBody()?.string()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    btnRegister.isEnabled = true
                    Toast.makeText(
                        this@RegistrasiActivity,
                        "Terjadi kesalahan koneksi: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}