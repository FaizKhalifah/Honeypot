package com.praktikum.honeypot.Data

data class SaleRecordRequest(
    val product_id: Int,
    val quantity: Int,
    val sale_date: String
)

data class SaleRecordResponse(
    val success: List<SaleSuccess>,
    val errors: List<SaleError>
)

data class SaleSuccess(
    val sale_id: Int,
    val partner_id: Int,
    val product_id: Int,
    val quantity: Int,
    val sale_date: String,
    val total_price: Int,
    val updatedAt: String,
    val createdAt: String
)

data class SaleError(
    val product_id: Int,
    val message: String,
    val available_stock: Int
) 