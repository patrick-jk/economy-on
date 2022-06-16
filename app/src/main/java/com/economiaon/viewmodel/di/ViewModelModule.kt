package com.economiaon.viewmodel.di

import com.economiaon.ui.navigation.financelist.FinancesListViewModel
import com.economiaon.viewmodel.RegisterActivityViewModel
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
            viewModel { RegisterActivityViewModel(get()) }
            viewModel { FinancesListViewModel(get()) }
        }
    }
}