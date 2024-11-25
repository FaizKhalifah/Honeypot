package com.praktikum.honeypot.Data

data class Partner(
    val partner_id : Int,
    val name:String,
    val address:String,
    val PartnerStocks: List<PartnerStock> = emptyList()
)