package com.economiaon.presentation.addfinance

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.domain.usecase.DeleteFinanceUseCase
import com.economiaon.domain.usecase.FindUserByIdUseCase
import com.economiaon.domain.usecase.SaveFinanceUseCase
import com.economiaon.domain.usecase.UpdateFinanceUseCase
import com.economiaon.domain.model.Finance
import com.economiaon.domain.model.User
import com.economiaon.presentation.statepattern.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AddFinanceViewModel(
    private val updateFinanceUseCase: UpdateFinanceUseCase,
    private val saveFinanceUseCase: SaveFinanceUseCase,
    private val findUserByIdUseCase: FindUserByIdUseCase,
    private val deleteFinanceUseCase: DeleteFinanceUseCase
) : ViewModel() {

    private val _isFinanceUpdated = MutableLiveData<Boolean>()
    val isFinanceUpdated: LiveData<Boolean> = _isFinanceUpdated

    private val _isFinanceCreated = MutableLiveData<Boolean>()
    val isFinanceCreated: LiveData<Boolean> = _isFinanceCreated

    private val _isFinanceDeleted = MutableLiveData<Boolean>()
    val isFinanceDeleted: LiveData<Boolean> = _isFinanceDeleted

    fun updateFinance(finance: Finance) = viewModelScope.launch {
        updateFinanceUseCase(finance)
            .catch {
                _isFinanceUpdated.postValue(false)
            }
            .collect {
                _isFinanceUpdated.postValue(true)
            }
    }

    fun saveFinance(finance: Finance) = viewModelScope.launch {
        saveFinanceUseCase(finance)
            .catch {
                _isFinanceCreated.postValue(false)
            }
            .collect {
                _isFinanceCreated.postValue(true)
            }
    }

    fun deleteFinance(id: String) = viewModelScope.launch {
        deleteFinanceUseCase(id)
            .catch {
                _isFinanceDeleted.postValue(false)
            }
            .collect {
                _isFinanceDeleted.postValue(true)
            }
    }
}