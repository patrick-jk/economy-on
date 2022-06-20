package com.economiaon.ui.navigation.financelist

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

class FinancesListViewModel(private val listFinancesByUserIdUseCase: ListFinancesByUserIdUseCase) :
    ViewModel() {

    private val _finances = MutableLiveData<State>()
    val finances: LiveData<State> = _finances

    fun getFinanceList(userId: Long) {
        viewModelScope.launch {
            listFinancesByUserIdUseCase.execute(userId)
                .onStart {
                    _finances.postValue(State.Loading)
                }
                .catch {
                    _finances.postValue(State.Error(it))
                }
                .collect {
                    _finances.postValue(State.Success(it))
                }
        }
    }

    sealed class State {
        object Loading : State()
        data class Success(val list: List<Finance>) : State()
        data class Error(val error: Throwable) : State()
    }
}