package com.example.qla_cimol
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
class RatingActivity : AppCompatActivity() {
    private lateinit var ratingBar: RatingBar
    private lateinit var tvNilai: TextView
    private lateinit var etUlasan: EditText
    private lateinit var btnKirim: Button
    private lateinit var tvHasil: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        ratingBar = findViewById(R.id.ratingBar)
        tvNilai = findViewById(R.id.tvNilaiRating)
        etUlasan = findViewById(R.id.etUlasan)
        btnKirim = findViewById(R.id.btnKirimUlasan)
        tvHasil = findViewById(R.id.tvHasilUlasan)

        // Tampilkan ulasan yang pernah disimpan
        tampilkanUlasan()

        // Ketika pengguna memilih bintang
        ratingBar.setOnRatingBarChangeListener {
                _, rating, _ ->

            val keterangan = when (rating.toInt()) {
                1 -> "Sangat Buruk"
                2 -> "Kurang Baik"
                3 -> "Cukup Baik"
                4 -> "Baik"
                5 -> "Sangat Baik"
                else -> "Pilih bintang 1-5"
            }

            tvNilai.text =
                "${rating.toInt()} Bintang - $keterangan"
        }

        // Tombol kirim ulasan
        btnKirim.setOnClickListener {
            kirimUlasan()
        }
    }

    private fun kirimUlasan() {

        val nilai = ratingBar.rating.toInt()
        val komentar = etUlasan.text.toString().trim()

        if (nilai == 0) {
            Toast.makeText(
                this,
                "Silakan pilih rating terlebih dahulu",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (komentar.isEmpty()) {
            etUlasan.error = "Komentar tidak boleh kosong"
            etUlasan.requestFocus()
            return
        }

        val preferences = getSharedPreferences(
            "LaundryReviews",
            MODE_PRIVATE
        )

        // Mengambil ulasan sebelumnya
        val ulasanLama = preferences.getString(
            "daftar_ulasan", ""
        ) ?: ""

        val ulasanBaru =
            "Rating: $nilai/5\nUlasan: $komentar"

        // Menambahkan ulasan baru
        val semuaUlasan = if (ulasanLama.isEmpty()) {
            ulasanBaru
        } else {
            "$ulasanBaru\n\n----------------\n\n$ulasanLama"
        }

        // Menyimpan ulasan
        preferences.edit()
            .putString("daftar_ulasan", semuaUlasan)
            .apply()

        Toast.makeText(
            this,
            "Terima kasih! Ulasan berhasil dikirim",
            Toast.LENGTH_LONG
        ).show()

        // Membersihkan form
        ratingBar.rating = 0f
        etUlasan.text.clear()
        tvNilai.text = "Pilih bintang 1-5"

        // Memperbarui tampilan
        tampilkanUlasan()
    }

    private fun tampilkanUlasan() {

        val preferences = getSharedPreferences(
            "LaundryReviews",
            MODE_PRIVATE
        )

        val semuaUlasan = preferences.getString(
            "daftar_ulasan",
            ""
        )

        if (semuaUlasan.isNullOrEmpty()) {
            tvHasil.text = "Belum ada ulasan."
        } else {
            tvHasil.text = semuaUlasan
        }
    }
}
