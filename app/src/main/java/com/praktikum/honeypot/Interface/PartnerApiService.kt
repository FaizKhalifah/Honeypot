package com.praktikum.honeypot.Interface

import com.praktikum.honeypot.Data.Partner
import retrofit2.http.GET

interface PartnerApiService {
    @GET("/api/partner/")
    suspend fun getPartners(): List<Partner>
}
