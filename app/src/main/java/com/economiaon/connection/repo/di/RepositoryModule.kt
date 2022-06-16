package com.economiaon.connection.repo.di

import com.economiaon.connection.repo.FinanceRepository
import com.economiaon.connection.repo.UserRepository
import com.economiaon.viewmodel.di.ViewModelModule
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object RepositoryModule {
    fun load() {
        loadKoinModules(repositoriesModule())
    }

    private fun repositoriesModule(): Module {
        return module {
            single {
                UserRepository(get())
            }
            single {
                FinanceRepository(get())
            }
        }
    }
}