package com.economiaon.repo

import com.economiaon.domain.Finance
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("getFinances/{user}")
    fun listFinances(@Path("user") user: String) : List<Finance>
}