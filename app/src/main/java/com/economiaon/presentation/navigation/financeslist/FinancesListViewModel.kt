package com.economiaon.presentation.navigation.financeslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.domain.usecase.ListFinancesByUserIdUseCase
import com.economiaon.presentation.statepattern.FinanceState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class FinancesListViewModel(
    private val listFinancesByUserIdUseCase: ListFinancesByUserIdUseCase,
) : ViewModel() {

    private val _finances = MutableLiveData<FinanceState>()
    val finances: LiveData<FinanceState> = _finances

    fun getFinanceList(userId: Long) {
        viewModelScope.launch {
            listFinancesByUserIdUseCase(userId)
                .onStart {
                    _finances.postValue(FinanceState.Loading)
                }
                .catch {
                    _finances.postValue(FinanceState.Error(it))
                }
                .collect {
                    _finances.postValue(FinanceState.Success(it))
                }
        }
    }
}