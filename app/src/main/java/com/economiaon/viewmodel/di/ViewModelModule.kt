package com.economiaon.viewmodel.di

import com.economiaon.ui.navigation.financelist.FinancesListViewModel
import com.economiaon.ui.navigation.profile.ProfileViewModel
import com.economiaon.usecase.ListFinancesByUserIdUseCase
import com.economiaon.viewmodel.AddFinanceViewModel
import com.economiaon.viewmodel.EditUserViewModel
import com.economiaon.viewmodel.LoginViewModel
import com.economiaon.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object ViewModelModule {
    fun load() {
        loadKoinModules(viewModelModule())
    }

    private fun viewModelModule(): Module {
        return module {
            viewModel { RegisterViewModel(get()) }
            viewModel { FinancesListViewModel(get()) }
            viewModel { LoginViewModel(get()) }
            viewModel { AddFinanceViewModel(get(), get()) }
            viewModel { ProfileViewModel(get()) }
            viewModel { EditUserViewModel(get()) }
            factory { ListFinancesByUserIdUseCase(get()) }
        }
    }
}