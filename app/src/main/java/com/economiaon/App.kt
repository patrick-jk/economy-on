package com.economiaon

import android.app.Application
import com.economiaon.connection.repo.di.RepositoryModule
import com.economiaon.connection.service.di.ApiModule
import com.economiaon.viewmodel.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
        }

        ApiModule.loadApiModule()
        ViewModelModule.load()
        RepositoryModule.load()
    }
}