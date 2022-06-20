package com.economiaon.data.connection.service

import com.economiaon.data.model.Finance
import com.economiaon.data.model.User
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users/{id}")
    fun findUserById(@Path("id") userId: Long) : Call<User>

    @GET("users/find")
    fun findUserByEmail(@Query("email") email: String) : Call<User>

    @POST("users")
    fun saveUser(@Body user: User) : Call<User>

    @PUT("users")
    fun updateUser(@Body user: User) : Call<User>

    @GET("finances/find")
    suspend fun getFinancesByUserId(@Query("userId") userId: Long) : List<Finance>

    @POST("finances")
    fun saveFinance(@Body finance: Finance) : Call<Finance>

    @PUT("finances")
    fun updateFinance(@Body finance: Finance) : Call<Nothing>

    @DELETE("finances/{id}")
    fun deleteFinance(@Path("id") financeId: Long): Call<Nothing>
}
