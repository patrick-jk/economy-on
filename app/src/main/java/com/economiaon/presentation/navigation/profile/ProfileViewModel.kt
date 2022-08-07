package com.economiaon.presentation.navigation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.domain.FindUserByIdUseCase
import com.economiaon.presentation.statepattern.UserState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ProfileViewModel(private val findUserByIdUseCase: FindUserByIdUseCase) : ViewModel() {
    private val _userInfo = MutableLiveData<UserState>()
    val userInfo: LiveData<UserState> = _userInfo

    fun getUserById(userId: Long) = viewModelScope.launch {
        findUserByIdUseCase(userId)
            .onStart {
                _userInfo.postValue(UserState.Loading)
            }
            .catch {
                _userInfo.postValue(UserState.Error(it))
            }
            .collect {
                _userInfo.postValue(UserState.Success(it))
            }
    }
}