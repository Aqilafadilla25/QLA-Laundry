package com.example.qla_cimol

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // A: Menunggu 2 detik
        lifecycleScope.launch {
            delay(2000)

            // B: Membaca status login
            val pref = getSharedPreferences(
                "user_pref",
                MODE_PRIVATE
            )

            val isLogin = pref.getBoolean("isLogin", false)

            // C: Menentukan halaman tujuan
            val tujuan = if (isLogin) {
                MainActivity::class.java
            } else {
                MainActivity::class.java
            }
            // D: Berpindah halaman
            startActivity(
                Intent(this@SplashScreenActivity, tujuan)
            )

            // E: Menutup Splash Screen
            finish()
        }
    }
}
