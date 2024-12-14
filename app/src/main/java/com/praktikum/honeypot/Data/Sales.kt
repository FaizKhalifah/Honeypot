package com.praktikum.honeypot.Data

data class SalesResponse(
    val partnerSales: Map<String, PartnerSales>,
    val productSales: Map<String, ProductSales>,
    val overallTotal: OverallTotal
)

data class PartnerSales(
    val total_quantity: Int,
    val total_revenue: Int,
    val products: Map<String, ProductDetails>
)

data class ProductDetails(
    val total_quantity: Int,
    val total_revenue: Int
)

data class ProductSales(
    val total_quantity: Int,
    val total_revenue: Int
)

data class OverallTotal(
    val total_quantity: Int,
    val total_revenue: Int
)

// Class helper untuk UI
data class SalesUIItem(
    val name: String,
    val quantity: Int,
    val revenue: Int
)
