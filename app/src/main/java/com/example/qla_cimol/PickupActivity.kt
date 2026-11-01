package com.example.qla_cimol
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class PickupActivity : AppCompatActivity() {
    private lateinit var etNama: EditText
    private lateinit var etHp: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etCatatan: EditText
    private lateinit var rgLayanan: RadioGroup
    private lateinit var btnTanggal: Button
    private lateinit var btnWaktu: Button
    private lateinit var btnKonfirmasi: Button

    private var tanggalDipilih = ""
    private var waktuDipilih = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pickup)

        etNama = findViewById(R.id.etNamaPickup)
        etHp = findViewById(R.id.etHpPickup)
        etAlamat = findViewById(R.id.etAlamatPickup)
        etCatatan = findViewById(R.id.etCatatanPickup)

        rgLayanan = findViewById(R.id.rgLayananPickup)

        btnTanggal = findViewById(R.id.btnTanggalPickup)
        btnWaktu = findViewById(R.id.btnWaktuPickup)
        btnKonfirmasi = findViewById(R.id.btnKonfirmasiPickup)

        // Pilih tanggal penjemputan
        btnTanggal.setOnClickListener {
            val calendar = Calendar.getInstance()

            val dialog = DatePickerDialog(
                this,
                { _, year, month, day ->
                    tanggalDipilih =
                        String.format("%02d-%02d-%04d",
                            day, month + 1, year)

                    btnTanggal.text = tanggalDipilih
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            dialog.datePicker.minDate =
                System.currentTimeMillis() - 1000

            dialog.show()
        }

        // Pilih waktu penjemputan
        btnWaktu.setOnClickListener {
            val calendar = Calendar.getInstance()

            val dialog = TimePickerDialog(
                this,
                { _, hour, minute ->
                    waktuDipilih =
                        String.format("%02d:%02d", hour, minute)

                    btnWaktu.text = waktuDipilih
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )

            dialog.show()
        }

        // Konfirmasi pesanan
        btnKonfirmasi.setOnClickListener {
            konfirmasiPesanan()
        }
    }

    private fun konfirmasiPesanan() {

        val nama = etNama.text.toString().trim()
        val hp = etHp.text.toString().trim()
        val alamat = etAlamat.text.toString().trim()
        val catatan = etCatatan.text.toString().trim()

        if (nama.isEmpty()) {
            etNama.error = "Nama wajib diisi"
            etNama.requestFocus()
            return
        }

        if (hp.length < 10) {
            etHp.error = "Masukkan nomor HP yang valid"
            etHp.requestFocus()
            return
        }

        if (alamat.isEmpty()) {
            etAlamat.error = "Alamat wajib diisi"
            etAlamat.requestFocus()
            return
        }

        if (tanggalDipilih.isEmpty()) {
            Toast.makeText(
                this, "Silakan pilih tanggal",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (waktuDipilih.isEmpty()) {
            Toast.makeText(
                this, "Silakan pilih waktu",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val layanan = if (
            rgLayanan.checkedRadioButtonId == R.id.rbJemputAntar
        ) {
            "Jemput dan Antar"
        } else {
            "Jemput saja"
        }

        // Simpan data pesanan di penyimpanan lokal
        val preferences = getSharedPreferences(
            "LaundryExpress",
            MODE_PRIVATE
        )

        preferences.edit()
            .putString("pickup_nama", nama)
            .putString("pickup_hp", hp)
            .putString("pickup_alamat", alamat)
            .putString("pickup_tanggal", tanggalDipilih)
            .putString("pickup_waktu", waktuDipilih)
            .putString("pickup_layanan", layanan)
            .putString("pickup_catatan", catatan)
            .apply()

        val ringkasan = """
            Nama: $nama
            
            Nomor HP: $hp
            
            Alamat: $alamat
            
            Tanggal: $tanggalDipilih
            
            Waktu: $waktuDipilih
            
            Layanan: $layanan
            
            Catatan: ${if (catatan.isEmpty()) "-" else catatan}
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Pesanan Berhasil!")
            .setMessage(ringkasan)
            .setPositiveButton("Selesai") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .show()
    }
}
