package com.economiaon.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.data.model.Finance
import com.economiaon.usecase.ListFinancesByUserIdUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class PresentChartViewModel(private val listFinances: ListFinancesByUserIdUseCase): ViewModel() {
    private val _financesList = MutableLiveData<State>()
    val financesList: LiveData<State> = _financesList

    fun getFinancesByUserId(userId: Long) {
        viewModelScope.launch {
            listFinances.execute(userId)
                .onStart {
                    _financesList.postValue(State.Loading)
                }
                .catch {
                    _financesList.postValue(State.Error(it))
                }
                .collect {
                    _financesList.postValue(State.Success(it))
                }

        }
    }

    sealed class State {
        object Loading : State()
        data class Success(val list: List<Finance>) : State()
        data class Error(val error: Throwable) : State()
    }
}