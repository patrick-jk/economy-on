package com.economiaon.presentation.addfinance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.domain.DeleteFinanceUseCase
import com.economiaon.domain.FindUserByIdUseCase
import com.economiaon.domain.SaveFinanceUseCase
import com.economiaon.domain.UpdateFinanceUseCase
import com.economiaon.domain.model.Finance
import com.economiaon.presentation.statepattern.UserState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
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

    private val _loggedUser = MutableLiveData<UserState>()
    val loggedUser: LiveData<UserState> = _loggedUser

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

    fun getUserById(userId: Long) = viewModelScope.launch {
        findUserByIdUseCase(userId)
            .catch {
                _loggedUser.postValue(UserState.Error(it))
            }
            .collect {
                _loggedUser.postValue(UserState.Success(it))
            }
    }

    fun deleteFinance(id: Long) = viewModelScope.launch {
        deleteFinanceUseCase(id)
            .catch {
                _isFinanceDeleted.postValue(false)
            }
            .collect {
                _isFinanceDeleted.postValue(true)
            }
    }
}