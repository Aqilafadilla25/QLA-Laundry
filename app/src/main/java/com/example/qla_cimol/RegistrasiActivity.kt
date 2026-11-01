
package com.example.qla_cimol

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

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

            val intent = Intent(this, VerificationActivity::class.java)
            intent.putExtra("nama", namaValue)
            intent.putExtra("hp", hpValue)
            intent.putExtra("username", usernameValue)
            intent.putExtra("password", passwordValue)
            startActivity(intent)
        }
    }
}
