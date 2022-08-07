package com.economiaon.presentation.di

import com.economiaon.presentation.addfinance.AddFinanceViewModel
import com.economiaon.presentation.edituser.EditUserViewModel
import com.economiaon.presentation.login.LoginViewModel
import com.economiaon.presentation.navigation.financeschart.FinancesChartViewModel
import com.economiaon.presentation.navigation.financeslist.FinancesListViewModel
import com.economiaon.presentation.navigation.home.HomeViewModel
import com.economiaon.presentation.navigation.profile.ProfileViewModel
import com.economiaon.presentation.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object PresentationModule {
    fun load() {
        loadKoinModules(viewModelModule())
    }

    private fun viewModelModule(): Module = module {
        viewModel { AddFinanceViewModel(get(), get(), get(), get()) }
        viewModel { EditUserViewModel(get()) }
        viewModel { FinancesListViewModel(get()) }
        viewModel { FinancesChartViewModel(get()) }
        viewModel { HomeViewModel(get(), get()) }
        viewModel { LoginViewModel(get()) }
        viewModel { ProfileViewModel(get()) }
        viewModel { RegisterViewModel(get(), get()) }
    }
}