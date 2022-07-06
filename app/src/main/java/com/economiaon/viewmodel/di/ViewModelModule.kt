package com.economiaon.viewmodel.di

import com.economiaon.ui.navigation.financelist.FinancesListViewModel
import com.economiaon.ui.navigation.financeschart.FinancesOverviewViewModel
import com.economiaon.ui.navigation.home.HomeViewModel
import com.economiaon.ui.navigation.profile.ProfileViewModel
import com.economiaon.usecase.ListFinancesByUserIdUseCase
import com.economiaon.viewmodel.*
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
            viewModel { PresentChartViewModel(get()) }
            viewModel { FutureChartViewModel(get()) }
            viewModel { FinancesOverviewViewModel(get()) }
            viewModel { HomeViewModel(get(), get()) }
            factory { ListFinancesByUserIdUseCase(get()) }
        }
    }
}