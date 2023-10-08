package com.economiaon.data.remote

import com.economiaon.data.model.CurrencyApiResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("convertcurrency")
    fun convertCurrency(@Query("want") toCurrency: String, @Query("have") fromCurrency: String, @Query("amount") value: Double) : Call<CurrencyApiResponse>

}