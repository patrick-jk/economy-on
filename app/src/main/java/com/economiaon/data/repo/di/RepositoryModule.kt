package com.economiaon.data.repo.di

import com.economiaon.data.repo.FinanceRepository
import com.economiaon.data.repo.UserRepository
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