package com.praktikum.honeypot.Data

data class Product(
    val product_id: Int,
    val name: String,
    val description: String,
    val stock: Int,
    val price_per_unit: Int,
    val image_url: String? // New field for image URL
)
