package com.example.qla_cimol

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Tombol 1: "Pesan Laundry →" (yang ada di banner)
        val btnBukaWeb = findViewById<Button>(R.id.btnBukaWeb)
        btnBukaWeb.setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            startActivity(intent)
        }

        // Tombol 2: "Buka Website Laundry" (tombol pink di bawah)
        val btnBukaWebView = findViewById<Button>(R.id.btnBukaWebView)
        btnBukaWebView.setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            startActivity(intent)
        }
    }
}
