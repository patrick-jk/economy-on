package com.economiaon.domain.di

import com.economiaon.domain.usecase.ConvertCurrencyUseCase
import com.economiaon.domain.usecase.DeleteFinanceUseCase
import com.economiaon.domain.usecase.FindUserByEmailUseCase
import com.economiaon.domain.usecase.FindUserByIdUseCase
import com.economiaon.domain.usecase.ListFinancesByUserIdUseCase
import com.economiaon.domain.usecase.SaveFinanceUseCase
import com.economiaon.domain.usecase.SaveUserUseCase
import com.economiaon.domain.usecase.UpdateFinanceUseCase
import com.economiaon.domain.usecase.UpdateUserUseCase
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
        factory { ConvertCurrencyUseCase(get()) }
    }
}