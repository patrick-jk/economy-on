package com.economiaon.presentation.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.domain.model.User
import com.economiaon.domain.usecase.FindUserByEmailUseCase
import com.economiaon.domain.usecase.SaveUserUseCase
import com.economiaon.presentation.statepattern.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val saveUserUseCase: SaveUserUseCase,
    private val findUserByEmailUseCase: FindUserByEmailUseCase
) : ViewModel() {

        private val _registerUser = MutableLiveData<State<User>>()
    val registerUser: LiveData<State<User>> = _registerUser

    private val _isUserAlreadyRegistered = MutableLiveData<Boolean>()
    val isUserAlreadyRegistered: LiveData<Boolean> = _isUserAlreadyRegistered


    fun saveUser(user: User) = viewModelScope.launch {
        saveUserUseCase(user)
            .onStart {
                _registerUser.postValue(State.Loading("Loading"))
            }
            .catch {
                _registerUser.postValue(State.Error(it))
            }
            .collect {
                _registerUser.postValue(State.Success(it))
            }
    }

    fun findUserByEmail(userEmail: String) = viewModelScope.launch {
        findUserByEmailUseCase(userEmail)
            .onStart {
            }
            .catch {
                _isUserAlreadyRegistered.postValue(false)
            }
            .collect {
                _isUserAlreadyRegistered.postValue(true)
            }
    }
}