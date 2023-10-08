package com.economiaon.presentation.navigation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.domain.model.User
import com.economiaon.domain.usecase.FindUserByIdUseCase
import com.economiaon.domain.usecase.ListFinancesByUserIdUseCase
import com.economiaon.presentation.statepattern.FinanceState
import com.economiaon.presentation.statepattern.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
    private val listFinancesByUserIdUseCase: ListFinancesByUserIdUseCase,
    private val findUserByIdUseCase: FindUserByIdUseCase
) : ViewModel() {

    private val _finances = MutableLiveData<FinanceState>()
    val finances: LiveData<FinanceState> = _finances

    private val _user = MutableLiveData<State<User>>()
    val user: LiveData<State<User>> = _user

    fun getFinanceList(userId: String) = viewModelScope.launch {
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

    fun getUserById(userId: String) = viewModelScope.launch {
        findUserByIdUseCase(userId)
            .onStart {
                _user.postValue(State.Loading("Loading..."))
            }
            .catch {
                _user.postValue(State.Error(it))
            }
            .collect {
                _user.postValue(State.Success(it))
            }
    }
}