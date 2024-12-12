package com.praktikum.honeypot.Data

data class Partner(
    val partner_id: Int,
    val name: String,
    val address: String,
    val imageUrl: String?,  // New attribute for partner image URL
    val PartnerStocks: List<PartnerStock> = emptyList()
)

