package com.example.currencynow

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class CurrencyResponse(
    val success: Boolean,
    val timestamp: Long,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)

interface ApiService {
    @GET("/api/latest?access_key=3cacba8dfd6328329c6a1bfe1e242ca8&base={base}")
    suspend fun getCurrencyList(@Query("base") String base): CurrencyResponse
}