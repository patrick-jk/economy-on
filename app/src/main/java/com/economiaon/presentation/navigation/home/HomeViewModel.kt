package com.economiaon.presentation.navigation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.domain.usecase.FindUserByIdUseCase
import com.economiaon.domain.usecase.ListFinancesByUserIdUseCase
import com.economiaon.presentation.statepattern.FinanceState
import com.economiaon.presentation.statepattern.UserState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
    private val listFinancesByUserIdUseCase: ListFinancesByUserIdUseCase,
    private val findUserByIdUseCase: FindUserByIdUseCase
) : ViewModel() {

    private val _finances = MutableLiveData<FinanceState>()
    val finances: LiveData<FinanceState> = _finances

    private val _user = MutableLiveData<UserState>()
    val user: LiveData<UserState> = _user

    fun getFinanceList(userId: Long) = viewModelScope.launch {
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

    fun getUserById(userId: Long) = viewModelScope.launch {
        findUserByIdUseCase(userId)
            .onStart {
                _user.postValue(UserState.Loading)
            }
            .catch {
                _user.postValue(UserState.Error(it))
            }
            .collect {
                _user.postValue(UserState.Success(it))
            }
    }
}