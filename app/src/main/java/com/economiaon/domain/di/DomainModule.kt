package com.economiaon.domain.di

import com.economiaon.domain.*
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object DomainModule {
    fun load() {
        loadKoinModules(useCaseModule())
    }

    private fun useCaseModule(): Module = module {
        factory { FindUserByEmailUseCase(get()) }
        factory { FindUserByIdUseCase(get()) }
        factory { ListFinancesByUserIdUseCase(get()) }
        factory { DeleteFinanceUseCase(get()) }
        factory { SaveFinanceUseCase(get()) }
        factory { SaveUserUseCase(get()) }
        factory { UpdateFinanceUseCase(get()) }
        factory { UpdateUserUseCase(get()) }
    }
}