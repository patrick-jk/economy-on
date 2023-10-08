package com.economiaon.presentation.navigation.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.domain.model.User
import com.economiaon.domain.usecase.FindUserByIdUseCase
import com.economiaon.presentation.statepattern.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ProfileViewModel(private val findUserByIdUseCase: FindUserByIdUseCase) : ViewModel() {
    private val _userInfo = MutableLiveData<State<User>>()
    val userInfo: LiveData<State<User>> = _userInfo

    fun getUserById(userId: String) = viewModelScope.launch {
        findUserByIdUseCase(userId)
            .onStart {
                _userInfo.postValue(State.Loading("Loading..."))
            }
            .catch {
                _userInfo.postValue(State.Error(it))
            }
            .collect {
                _userInfo.postValue(State.Success(it))
            }
    }
}