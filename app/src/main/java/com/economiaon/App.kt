package com.economiaon

import android.app.Application
import com.economiaon.connection.service.di.ApiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
        }

        ApiModule.loadApiModule()
    }
}