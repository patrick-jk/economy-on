package com.economiaon.data.connection.service

import com.economiaon.data.model.Finance
import com.economiaon.data.model.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    @GET("users/{id}")
    fun findUserById(@Path("id") userId: Long) : Call<User>

    @GET("users/find")
    fun findUserByEmail(@Query("email") email: String) : Call<User>

    @POST("users")
    fun saveUser(@Body user: User) : Call<User>

    @PUT("users")
    fun updateUser(@Body user: User) : Call<Nothing>

    @GET("finances/find")
    suspend fun getFinancesByUserId(@Query("userId") userId: Long) : List<Finance>

    @POST("finances")
    fun saveFinance(@Body finance: Finance) : Call<Finance>

    @PUT("finances")
    fun updateFinance(@Body finance: Finance) : Call<Nothing>

    @DELETE("finances/{id}")
    fun deleteFinance(@Path("id") financeId: Long): Call<Void>

    companion object {
        private val apiService by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(ApiService::class.java)
        }

        fun getInstance(): ApiService {
            return apiService
        }
    }
}
