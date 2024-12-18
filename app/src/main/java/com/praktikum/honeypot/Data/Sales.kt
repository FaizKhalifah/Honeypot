package com.praktikum.honeypot.Data

data class SalesResponse(
    val monthlySales: Map<String, MonthlySalesData>,
    val overallTotal: OverallTotal
)

data class MonthlySalesData(
    val partnerSales: Map<String, PartnerSales>,
    val productSales: Map<String, ProductSales>,
    val monthlyTotal: MonthlyTotal
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

data class MonthlyTotal(
    val total_quantity: Int,
    val total_revenue: Int
)

data class OverallTotal(
    val total_quantity: Int,
    val total_revenue: Int
)

// Class helper untuk UI
data class MonthlySalesUIItem(
    val month: String,
    val totalQuantity: Int,
    val totalRevenue: Int,
    val productSales: List<SalesUIItem>,
    val partnerSales: List<SalesUIItem>
)

data class SalesUIItem(
    val name: String,
    val quantity: Int,
    val revenue: Int
)
