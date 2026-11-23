package com.example.qla_cimol.data.model

data class OrderModel(
    val id: Int? = null,
    val nama_pelanggan: String,
    val jenis_layanan: String,
    val berat_kg: Double,
    val status: String
)