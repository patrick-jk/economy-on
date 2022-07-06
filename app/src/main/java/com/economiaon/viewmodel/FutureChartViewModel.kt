package com.economiaon.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.ui.navigation.financelist.FinancesListViewModel
import com.economiaon.usecase.ListFinancesByUserIdUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class FutureChartViewModel(private val listFinances: ListFinancesByUserIdUseCase): ViewModel() {
    private val _financesList = MutableLiveData<FinancesListViewModel.State>()
    val financesList: LiveData<FinancesListViewModel.State> = _financesList

    fun getFinancesByUserId(userId: Long) {
        viewModelScope.launch {
            listFinances.execute(userId)
                .onStart {
                    _financesList.postValue(FinancesListViewModel.State.Loading)
                }
                .catch {
                    _financesList.postValue(FinancesListViewModel.State.Error(it))
                }
                .collect {
                    _financesList.postValue(FinancesListViewModel.State.Success(it))
                }

        }
    }
}