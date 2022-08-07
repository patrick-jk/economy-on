package com.economiaon.data.di

import android.util.Log
import com.economiaon.data.ApiService
import com.economiaon.data.UserPreferences
import com.economiaon.data.repo.FinanceRepository
import com.economiaon.data.repo.UserRepository
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataModule {

    private const val OK_HTTP = "OkHttp"

    fun load() {
        loadKoinModules(apiModule() + repositoriesModule())
    }

    private fun apiModule(): Module {
        return module {
            single {
                GsonConverterFactory.create(GsonBuilder().create())
            }
            single {
                val interceptor = HttpLoggingInterceptor {
                    Log.i(OK_HTTP, it)
                }
                interceptor.level = HttpLoggingInterceptor.Level.BODY

                OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()
            }
            single {
                createApiService<ApiService>(get(), get())
            }
        }
    }

    private inline fun <reified T> createApiService(client: OkHttpClient, factory: GsonConverterFactory): T {
        return Retrofit.Builder()
            .baseUrl("http://192.168.100.137:8080/api/")
            .client(client)
            .addConverterFactory(factory)
            .build().create(T::class.java)
    }

    private fun repositoriesModule(): Module {
        return module {
            single {
                UserRepository(get())
            }
            single {
                FinanceRepository(get())
            }
            single {
                UserPreferences(get())
            }
        }
    }
}