package com.economiaon.presentation.navigation.financeschart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.domain.model.Finance
import com.economiaon.domain.usecase.ListFinancesByUserIdUseCase
import com.economiaon.presentation.statepattern.FinanceState
import com.economiaon.presentation.statepattern.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class FinancesChartViewModel(private val listFinances: ListFinancesByUserIdUseCase) : ViewModel() {
    private val _financesList = MutableLiveData<FinanceState>()
    val financesList: LiveData<FinanceState> = _financesList

    fun getFinancesByUserId(userId: String) {
        viewModelScope.launch {
            listFinances(userId)
                .onStart {
                    _financesList.postValue(FinanceState.Loading)
                }
                .catch {
                    _financesList.postValue(FinanceState.Error(it))
                }
                .collect {
                    _financesList.postValue(FinanceState.Success(it))
                }
        }
    }
}