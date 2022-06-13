package com.economiaon.connection.service

import com.economiaon.domain.Finance
import com.economiaon.domain.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("users/{id}")
    suspend fun findUserById(@Path("id") userId: Long) : Response<User>

    @GET("users/find")
    suspend fun findUserByEmail(@Query("email") email: String) : Response<User>

    @POST("users")
    suspend fun saveUser(@Body user: User) : Call<User>

    @PUT("users")
    suspend fun updateUser(@Body user: User) : Response<User>

    @GET("finances/find")
    suspend fun getFinancesByUserId(@Query("userId") userId: Long) : List<Finance>

    @POST("finances")
    suspend fun saveFinance(@Body finance: Finance) : Response<Finance>

    @PUT("finances")
    suspend fun updateFinance(@Body finance: Finance) : Response<Finance>

    @DELETE("finances/{id}")
    suspend fun deleteFinance(@Path("id") userId: Long)
}
