package com.example.qla_cimol

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                    this,
                    "Mohon isi Username dan Password!",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (
                username == "aqila" && password == "123"
            ) {

                // Simpan status login
                val pref = getSharedPreferences(
                    "user_pref",
                    MODE_PRIVATE
                )

                pref.edit()
                    .putBoolean("isLogin", true)
                    .apply()

                Toast.makeText(
                    this,
                    "Login Berhasil!",
                    Toast.LENGTH_SHORT
                ).show()

                // Pindah ke halaman Detail
                val intent = Intent(
                    this,
                    DetailActivity::class.java
                )
                startActivity(intent)
                finish()

            } else {

                // Username atau password salah
                Toast.makeText(
                    this,
                    "Username atau password salah!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}