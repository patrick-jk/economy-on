package com.economiaon.connection.service.di

import android.util.Log
import com.economiaon.connection.service.ApiService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiModule {

    private const val OK_HTTP = "OkHttp"

    fun loadApiModule() {
        loadKoinModules(apiModule())
    }

    private fun apiModule(): Module {
        return module {
            single {
                createApiService<ApiService>(get(), get())
            }
            single {
                GsonConverterFactory.create(GsonBuilder().create())
            }
            single {
                val interceptor = HttpLoggingInterceptor {
                    Log.e(OK_HTTP, it)
                }
                interceptor.level = HttpLoggingInterceptor.Level.BODY

                OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()
            }
        }
    }

    private inline fun <reified T> createApiService(client: OkHttpClient, factory: GsonConverterFactory): T {
        return Retrofit.Builder()
            .baseUrl("192.168.100.137:8080/api/")
            .client(client)
            .addConverterFactory(factory)
            .build().create(T::class.java)
    }
}