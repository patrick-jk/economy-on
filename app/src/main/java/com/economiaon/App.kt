package com.economiaon

import android.app.Application
import com.economiaon.data.di.DataModule
import com.economiaon.domain.di.DomainModule
import com.economiaon.presentation.di.PresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
        }

        DataModule.load()
        DomainModule.load()
        PresentationModule.load()
    }
}