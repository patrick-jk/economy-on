package com.economiaon.data.di

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.economiaon.data.remote.ApiService
import com.economiaon.data.datastore.UserPreferences
import com.economiaon.data.domain.FinanceDataSource
import com.economiaon.data.remote.impl.FirebaseFinanceDataSource
import com.economiaon.data.remote.impl.FirebaseUserDataSource
import com.economiaon.data.domain.UserDataSource
import com.economiaon.data.local.AppDatabase
import com.economiaon.data.local.impl.RoomFinanceDataSource
import com.economiaon.data.local.impl.RoomUserDataSource
import com.economiaon.data.repo.CurrencyRepository
import com.economiaon.data.repo.FinanceRepository
import com.economiaon.data.repo.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataModule {

    private const val OK_HTTP = "OkHttp"

    fun load() {
        loadKoinModules(apiModule() + repositoriesModule() + dataSourceModules() + databaseModules())
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
            .baseUrl("https://api.api-ninjas.com/v1/")
            .client(client)
            .addConverterFactory(factory)
            .build().create(T::class.java)
    }

    private fun repositoriesModule(): Module {
        return module {
            single {
                UserRepository(get(), get())
            }
            single {
                FinanceRepository(get(), get())
            }
            single {
                CurrencyRepository(get())
            }
        }
    }

    private fun dataSourceModules(): Module {
        return module {
            single { FirebaseUserDataSource(get(), get()) } bind UserDataSource::class
            single { FirebaseFinanceDataSource(get()) } bind FinanceDataSource::class
            single { FirebaseAuth.getInstance() }
            single { FirebaseFirestore.getInstance() }


            single {
                val userDao = get<AppDatabase>().userDao()
                RoomUserDataSource(userDao)
            } bind UserDataSource::class
            single {
                val financeDao = get<AppDatabase>().financeDao()
                RoomFinanceDataSource(financeDao)
            } bind FinanceDataSource::class
        }
    }

    private fun databaseModules(): Module {
        return module {
            single {
                createLocalDatabase(get())
            }
            single {
                UserPreferences(get())
            }
        }
    }

    private fun createLocalDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "oakley-saves.db")
            .build()
    }
}