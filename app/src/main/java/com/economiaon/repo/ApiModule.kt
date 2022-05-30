package com.economiaon.repo

import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit

object ApiModule {

    fun loadApiModule() {
        loadKoinModules(apiModule())
    }

    private fun apiModule(): Module {
        return module {
            single {
                createApiService<ApiService>()
            }
            //TODO Import GsonConverterFactory and OkHttpClient and create modules
        }
    }

    private inline fun <reified T> createApiService(): T {
        return Retrofit.Builder()
            .baseUrl("127.0.0.1/apiCall?=")
            .build().create(T::class.java)
    }
}