package com.economiaon.data.remote

import com.economiaon.domain.model.Finance
import com.economiaon.domain.model.User
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users/{id}")
    suspend fun findUserById(@Path("id") userId: Long) : User

    @GET("users/find")
    fun findUserByEmail(@Query("email") email: String) : Call<User>

    @POST("users")
    fun saveUser(@Body user: User) : Call<User>

    @PUT("users")
    suspend fun updateUser(@Body user: User) : Void

    @GET("finances/find")
    suspend fun getFinancesByUserId(@Query("userId") userId: Long) : List<Finance>

    @POST("finances")
    suspend fun saveFinance(@Body finance: Finance) : Finance

    @PUT("finances")
    suspend fun updateFinance(@Body finance: Finance) : Void

    @DELETE("finances/{id}")
    suspend fun deleteFinance(@Path("id") financeId: Long) : Void
}
